package ca.openosp.openo.tickler.dto;

import java.io.Serializable;

/**
 * Data Transfer Object for Tickler links in list views.
 * <p>
 * This DTO provides a lightweight representation of tickler link data,
 * used for batch loading links to avoid N+1 query problems.
 * </p>
 *
 * @since 2026-01-30
 */
public class TicklerLinkDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer ticklerNo;
    private String tableName;
    private Long tableId;

    /**
     * Default constructor required for frameworks.
     */
    public TicklerLinkDTO() {
    }

    /**
     * Constructor for JPQL projection queries.
     *
     * @param id Integer the link ID
     * @param ticklerNo Integer the parent tickler ID
     * @param tableName String the linked table name (determines link type)
     * @param tableId Long the ID in the linked table
     */
    public TicklerLinkDTO(Integer id, Integer ticklerNo, String tableName, Long tableId) {
        this.id = id;
        this.ticklerNo = ticklerNo;
        this.tableName = tableName;
        this.tableId = tableId;
    }

    // Getters and Setters

    /**
     * Returns the link ID.
     *
     * @return Integer the unique identifier for this link
     * @since 2026-01-30
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the link ID.
     *
     * @param id Integer the unique identifier for this link
     * @since 2026-01-30
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the parent tickler ID.
     *
     * @return Integer the tickler ID this link belongs to
     * @since 2026-01-30
     */
    public Integer getTicklerNo() {
        return ticklerNo;
    }

    /**
     * Sets the parent tickler ID.
     *
     * @param ticklerNo Integer the tickler ID this link belongs to
     * @since 2026-01-30
     */
    public void setTicklerNo(Integer ticklerNo) {
        this.ticklerNo = ticklerNo;
    }

    /**
     * Returns the linked table name that determines the link type.
     *
     * @return String the table name (e.g., "HL7", "document", "HRM")
     * @since 2026-01-30
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the linked table name.
     *
     * @param tableName String the table name that determines link type
     * @since 2026-01-30
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Returns the ID of the linked record in the target table.
     *
     * @return Long the record ID in the linked table
     * @since 2026-01-30
     */
    public Long getTableId() {
        return tableId;
    }

    /**
     * Sets the ID of the linked record in the target table.
     *
     * @param tableId Long the record ID in the linked table
     * @since 2026-01-30
     */
    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
}
