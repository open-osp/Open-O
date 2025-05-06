//CHECKSTYLE:OFF
/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.PMmodule.exception.BedReservedException;
import org.oscarehr.PMmodule.exception.DuplicateBedNameException;
import org.oscarehr.PMmodule.exception.DuplicateRoomNameException;
import org.oscarehr.PMmodule.exception.RoomHasActiveBedsException;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.*;
import org.oscarehr.managers.BedManager;
import org.oscarehr.managers.BedDemographicManager;
import org.oscarehr.managers.RoomManager;
import org.oscarehr.managers.RoomDemographicManager;
import org.oscarehr.util.SpringUtils;

/**
 * Responsible for managing beds
 */
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class BedManager2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private static final String FORWARD_MANAGE = "manage";

    private BedManager bedManager = SpringUtils.getBean(BedManager.class);

    private ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);

    private RoomManager roomManager = SpringUtils.getBean(RoomManager.class);

    private FacilityDao facilityDao = SpringUtils.getBean(FacilityDao.class);

    private BedDemographicManager bedDemographicManager = SpringUtils.getBean(BedDemographicManager.class);
    private RoomDemographicManager roomDemographicManager = SpringUtils.getBean(RoomDemographicManager.class);

    public void setFacilityDao(FacilityDao facilityDao) {
        this.facilityDao = facilityDao;
    }

    public String execute() {
        // dispatch to correct method based on which button was selected
        // Please don't make changes that causes addRoom and addBed button not working any more!
        if ("".equals(request.getParameter("submit.saveRoom")) == false)
            return saveRooms();
        else if (request.getParameter("submit.deleteRoom") != null) return deleteRoom();
        else if ("".equals(request.getParameter("submit.addRoom")) == false)
            return addRooms();
        else if ("".equals(request.getParameter("submit.saveBed")) == false)
            return saveBeds();
        else if (request.getParameter("submit.deleteBed") != null) return deleteBed();
        else if ("".equals(request.getParameter("submit.addBed")) == false)
            return addBeds();
        else return manage();
    }

    public String manage() {
        Integer facilityId = Integer.valueOf(request.getParameter("facilityId"));
        Facility facility = facilityDao.find(facilityId);

        this.setFacilityId(facilityId);
        this.setRooms(roomManager.getRooms(facilityId));
        this.setAssignedBedRooms(roomManager.getAssignedBedRooms(facilityId));
        this.setRoomTypes(roomManager.getRoomTypes());
        this.setNumRooms(1);

        if (this.getBedRoomFilterForBed() == null) {
            Room[] room = this.getRooms();
            if (room != null && room.length > 0) {
                this.setBedRoomFilterForBed(room[0].getId());
            }
        }
        // this.setBeds(bedManager.getBedsByFacility(facilityId, null, false));
        List<Bed> lst = bedManager.getBedsByFilter(facilityId, this.getBedRoomFilterForBed(), null, false);
        this.setBeds(lst.toArray(new Bed[lst.size()]));

        this.setBedTypes(bedManager.getBedTypes());
        this.setNumBeds(1);
        this.setPrograms(programManager.getBedPrograms(facilityId));
        this.setFacility(facility);
        Map statusNames = new HashMap();
        statusNames.put("1", "Active");
        statusNames.put("0", "Inactive");
        statusNames.put("2", "Any");
        this.setRoomStatusNames(statusNames);
        this.setBedStatusNames(statusNames);

        return FORWARD_MANAGE;
    }

    public String manageFilter() {

        Integer facilityId = Integer.valueOf(request.getParameter("facilityId"));
        Facility facility = facilityDao.find(facilityId);

        this.setFacilityId(facilityId);
        this.setRooms(this.getRooms());
        this.setAssignedBedRooms(roomManager.getAssignedBedRooms(facilityId));
        this.setRoomTypes(roomManager.getRoomTypes());
        this.setNumRooms(1);
        this.setBeds(this.getBeds());
        this.setBedTypes(bedManager.getBedTypes());
        this.setNumBeds(1);
        this.setPrograms(programManager.getBedPrograms(facilityId));
        this.setFacility(facility);
        Map statusNames = new HashMap();
        statusNames.put("1", "Active");
        statusNames.put("0", "Inactive");
        statusNames.put("2", "Any");
        this.setRoomStatusNames(statusNames);
        this.setBedStatusNames(statusNames);

        return FORWARD_MANAGE;
    }

    public String saveRooms() {
        Room[] rooms = this.getRooms();

        // detect check box false
        for (int i = 0; i < rooms.length; i++) {
            if (request.getParameter("rooms[" + i + "].active") == null) {
                rooms[i].setActive(false);
            }
        }
        try {
            roomManager.saveRooms(rooms);
        } catch (DuplicateRoomNameException e) {
            addActionMessage(getText("room.duplicate.name.error", e.getMessage()));
        }

        return manage();
    }

    public String deleteRoom() {
        Integer roomId = this.getRoomToDelete();

        // (1)Check whether any client is assigned to this room ('room_demographic' table)->
        // if yes, disallow room delete and display message.
        // (2)if no client assigned, check whether any beds assigned ('bed' table) ->
        // if some bed assigned, retrieve all beds assigned to this room -> delete them all <-- ???
        // (3)then delete this room ('room' table)
        try {
            List<RoomDemographic> roomDemographicList = roomDemographicManager.getRoomDemographicByRoom(roomId);

            if (roomDemographicList != null && !roomDemographicList.isEmpty()) {
                throw new RoomHasActiveBedsException("The room has client(s) assigned to it and cannot be removed.");
            }

            Bed[] beds = bedManager.getBedsForDeleteByRoom(roomId);

            if (beds != null && beds.length > 0) {

                for (int i = 0; i < beds.length; i++) {
                    bedManager.deleteBed(beds[i]);
                }
            }

            Room room = roomManager.getRoom(roomId);
            roomManager.deleteRoom(room);

        } catch (RoomHasActiveBedsException e) {
            addActionMessage(getText("room.active.beds.error", e.getMessage()));
        }

        return manage();
    }

    public String saveBeds() {
        Bed[] beds = this.getBeds();

        for (int i = 0; i < beds.length; i++) {
            if (request.getParameter("beds[" + i + "].active") == null) {
                beds[i].setActive(false);
            }
        }
        Room[] rooms = roomManager.getUnfilledRoomIds(beds);
        if (rooms == null) {
            rooms = this.getRooms();
            if (rooms == null) {
                //log.error("saveBeds(): No beds are assigned to rooms.");
            }
        }
        try {
            beds = bedManager.getBedsForUnfilledRooms(rooms, beds);
            bedManager.saveBeds(beds);

        } catch (BedReservedException e) {
            addActionMessage(getText("bed.reserved.error", e.getMessage()));
        } catch (DuplicateBedNameException e) {
            addActionMessage(getText("bed.duplicate.name.error", e.getMessage()));
        }

        return manage();
    }

    public String deleteBed() {
        Integer bedId = this.getBedToDelete();
        // (1)Check whether any client is assigned to this bed ('bed_demographic' table)->
        // if yes, disallow bed delete and display message.
        // (2)if no client assigned, delete this bed ('bed' table)

        try {

            BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByBed(bedId);

            if (bedDemographic != null) {
                throw new BedReservedException("The bed has client assigned to it and cannot be removed.");
            }

            Bed bed = bedManager.getBedForDelete(bedId);
            bedManager.deleteBed(bed);

        } catch (BedReservedException e) {
            addActionMessage(getText("bed.reserved.error", e.getMessage()));
        }

        return manage();
    }

    public String addRooms() {

        Integer numRooms = this.getNumRooms();

/*????what is roomlines used for?
        Integer roomslines = 0;
        if ("".equals(request.getParameter("roomslines")) == false) {
            roomslines = Integer.valueOf(request.getParameter("roomslines"));
        }

        if (numRooms != null) {
            if (numRooms <= 0) {
                numRooms = 0;
            }
            else if (numRooms + roomslines > 10) {
                numRooms = 10 - roomslines;
            }
        }
*/
        if (numRooms != null && numRooms > 0) {
            roomManager.addRooms(this.getFacilityId(), numRooms);

        }

        return manage();
    }

    public String addBeds() {
        Integer facilityId = Integer.valueOf(request.getParameter("facilityId"));

        Integer numBeds = this.getNumBeds();
        Integer roomId = this.getBedRoomFilterForBed();

        int occupancyOfRoom = roomManager.getRoom(roomId).getOccupancy().intValue();

        //bedslines is the current total number of bed in the room.
        Integer bedslines = 0;
        if ("".equals(request.getParameter("bedslines")) == false) {
            bedslines = Integer.valueOf(request.getParameter("bedslines"));
        }

        if (numBeds != null) {
            if (numBeds <= 0) {
                numBeds = 0;
            } else if (numBeds + bedslines > occupancyOfRoom) {
                numBeds = occupancyOfRoom - bedslines;
            }
        }

        if (numBeds != null && numBeds > 0) {
            try {
                bedManager.addBeds(facilityId, roomId, numBeds);
            } catch (BedReservedException e) {
                addActionMessage(getText("bed.reserved.error", e.getMessage()));
            }
        } else {
            addActionMessage("The number of the beds in this room already reaches the maximum.");
        }

        return manage();
    }

    public String doRoomFilter() {
        Integer facilityId = Integer.valueOf(request.getParameter("facilityId"));

        Integer roomStatus = this.getRoomStatusFilter();
        Integer roomFilteredProgram = this.getBedProgramFilterForRoom();
        Boolean roomStatusBoolean = new Boolean(false);

        if (roomStatus.intValue() == 1) {
            roomStatusBoolean = new Boolean(true);
        } else if (roomStatus.intValue() == 0) {
            roomStatusBoolean = new Boolean(false);
        } else {
            roomStatusBoolean = null;
        }

        if (roomFilteredProgram.intValue() == 0) {
            roomFilteredProgram = null;
        }
        Room[] filteredRooms = roomManager.getRooms(facilityId, roomFilteredProgram, roomStatusBoolean);

        this.setRooms(filteredRooms);


        return manageFilter();
    }

    public String doBedFilter() {
        Integer facilityId = Integer.valueOf(request.getParameter("facilityId"));

        Integer bedStatus = this.getBedStatusFilter();
        Integer bedFilteredProgram = this.getBedRoomFilterForBed();
        Boolean bedStatusBoolean = new Boolean(false);

        List<Bed> filteredBeds = new ArrayList<Bed>();

        if (bedStatus.intValue() == 1) {
            bedStatusBoolean = new Boolean(true);
        } else if (bedStatus.intValue() == 0) {
            bedStatusBoolean = new Boolean(false);
        } else {
            bedStatusBoolean = null;
        }

        if (bedFilteredProgram.intValue() == 0) {
            bedFilteredProgram = null;
        }
        /*
         * List<Bed> filteredBedsList = null; Room[] filteredRooms = roomManager.getAssignedBedRooms(facilityId, bedFilteredProgram, bedStatusBoolean); for(int i=0; filteredRooms != null && i < filteredRooms.length; i++){
         *
         * if(filteredRooms[i] != null){ filteredBedsList = bedManager.getBedsByFilter(facilityId, filteredRooms[i].getId(), bedStatusBoolean, false); filteredBeds.addAll(filteredBedsList); } }
         */

        filteredBeds = bedManager.getBedsByFilter(facilityId, this.getBedRoomFilterForBed(), bedStatusBoolean, false);

        this.setBeds(filteredBeds.toArray(new Bed[filteredBeds.size()]));

        return manageFilter();
    }

    public void setBedManager(BedManager bedManager) {
        this.bedManager = bedManager;
    }

    public void setProgramManager(ProgramManager mgr) {
        this.programManager = mgr;
    }

    public void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    private Integer facilityId;
    private Facility facility;
    private Integer numRooms;
    private Integer numBeds;
    private Room[] rooms;
    private Room[] assignedBedRooms;
    private RoomType[] roomTypes;
    private Bed[] beds;
    private BedType[] bedTypes;
    private Program[] programs;
    private Integer roomToDelete;
    private Integer bedToDelete;
    private Integer roomStatusFilter;
    private Integer bedStatusFilter;
    private Integer bedProgramFilterForRoom;
    private Integer bedRoomFilterForBed;
    private Map roomStatusNames;
    private Map bedStatusNames;


    public Integer getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Integer facilityId) {
        this.facilityId = facilityId;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Integer getNumRooms() {
        return numRooms;
    }

    public void setNumRooms(Integer numRooms) {
        this.numRooms = numRooms;
    }

    public Integer getNumBeds() {
        return numBeds;
    }

    public void setNumBeds(Integer numBeds) {
        this.numBeds = numBeds;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }

    public RoomType[] getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(RoomType[] roomTypes) {
        this.roomTypes = roomTypes;
    }

    public Bed[] getBeds() {
        return beds;
    }

    public void setBeds(Bed[] beds) {
        this.beds = beds;
    }

    public BedType[] getBedTypes() {
        return bedTypes;
    }

    public void setBedTypes(BedType[] bedTypes) {
        this.bedTypes = bedTypes;
    }

    public Program[] getPrograms() {
        return programs;
    }

    public void setPrograms(Program[] programs) {
        this.programs = programs;
    }

    public Room[] getAssignedBedRooms() {
        return assignedBedRooms;
    }

    public void setAssignedBedRooms(Room[] assignedBedRooms) {
        this.assignedBedRooms = assignedBedRooms;
    }

    public Integer getBedToDelete() {
        return bedToDelete;
    }

    public void setBedToDelete(Integer bedToDelete) {
        this.bedToDelete = bedToDelete;
    }

    public Integer getRoomToDelete() {
        return roomToDelete;
    }

    public void setRoomToDelete(Integer roomToDelete) {
        this.roomToDelete = roomToDelete;
    }

    public Integer getBedRoomFilterForBed() {
        return bedRoomFilterForBed;
    }

    public void setBedRoomFilterForBed(Integer bedRoomFilterForBed) {
        this.bedRoomFilterForBed = bedRoomFilterForBed;
    }

    public Integer getBedProgramFilterForRoom() {
        return bedProgramFilterForRoom;
    }

    public void setBedProgramFilterForRoom(Integer bedProgramFilterForRoom) {
        this.bedProgramFilterForRoom = bedProgramFilterForRoom;
    }

    public Integer getBedStatusFilter() {
        return bedStatusFilter;
    }

    public void setBedStatusFilter(Integer bedStatusFilter) {
        this.bedStatusFilter = bedStatusFilter;
    }

    public Integer getRoomStatusFilter() {
        return roomStatusFilter;
    }

    public void setRoomStatusFilter(Integer roomStatusFilter) {
        this.roomStatusFilter = roomStatusFilter;
    }

    public Map getBedStatusNames() {
        return bedStatusNames;
    }

    public void setBedStatusNames(Map bedStatusNames) {
        this.bedStatusNames = bedStatusNames;
    }

    public Map getRoomStatusNames() {
        return roomStatusNames;
    }

    public void setRoomStatusNames(Map roomStatusNames) {
        this.roomStatusNames = roomStatusNames;
    }

}
