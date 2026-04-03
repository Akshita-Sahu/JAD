package org.example.jfranalyzerbackend.util;

public class PagingRequest {
    // 1
    private int page;

    // ，0
    private int pageSize;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    /**
     * 
     *
     * @param page     ，1
     * @param pageSize ，0
     */
    public PagingRequest(int page, int pageSize) {
//        Validate.isTrue(page >= 1 && pageSize >= 1);
        this.page = page;
        this.pageSize = pageSize;
    }

    /**
     * @return （），0
     */
    public int from() {
        return (page - 1) * pageSize;
    }

    /**
     * @param totalSize 
     * @return （）
     */
    public int to(int totalSize) {
        return Math.min(from() + pageSize, totalSize);
    }
}
