package com.cartest.pro.pojo;


import java.io.Serializable;
import java.util.List;

public class PageHelper<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<T> pageData;

    private int totalSize;  //总数

    private int curPage;    //当前页

    private int pageSize;   //每页数目

    private int startRow;

    private int totalPage;  //总页数

    public PageHelper() {
    }

    public PageHelper(List<T> pageData) {
        this.totalSize = 0;
        this.curPage = 0;
        this.pageSize = 0;
        this.pageData = pageData;
    }

    public PageHelper(int totalSize, int curPage, int pageSize,List<T> pageData) {
        this.totalSize = totalSize;
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.pageData = pageData;
        initRow();
    }

    protected void initRow() {
        this.totalPage = (totalSize + pageSize - 1) / pageSize;
        this.curPage = Math.max(1, Math.min(totalPage, curPage));
        this.startRow = (curPage - 1) * pageSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public List<T> getPageData() {
        return pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}

