//CHECKSTYLE:OFF
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.eform.upload;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.OscarProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageUpload2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private final SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public String execute() {

        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "w", null)) {
            throw new SecurityException("missing required security object (_eform)");
        }

        try {
            if (!imageFileName.matches("^[a-zA-Z0-9._-]+$")) {
                throw new IllegalArgumentException("Invalid image file name");
            }
            
            OutputStream fos = ImageUpload2Action.getEFormImageOutputStream(imageFileName);
            InputStream fis = Files.newInputStream(image.toPath());
            byte[] buffer = new byte[4096]; 
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.flush();

            request.setAttribute("status", "uploaded");
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return SUCCESS;
    }

    public static OutputStream getEFormImageOutputStream(String imageFileName) throws IOException {
        Path path = Paths.get(OscarProperties.getInstance().getEformImageDirectory(), imageFileName);
        return Files.newOutputStream(path);
        
    }

    public static File getImageFolder() throws IOException {
        File imageFolder = new File(OscarProperties.getInstance().getEformImageDirectory() + "/");
        if (!imageFolder.exists() && !imageFolder.mkdirs())
            throw new IOException("Could not create directory " + imageFolder.getAbsolutePath() + " check permissions and ensure the correct EFORM_IMAGES_DIR property is set in the properties file");
        return imageFolder;
    }

    private File image;

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    private String imageFileName;    
    private String imageFileContentType; 

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public void setImageFileContentType(String imageFileContentType) {
        this.imageFileContentType = imageFileContentType;
    }
}
