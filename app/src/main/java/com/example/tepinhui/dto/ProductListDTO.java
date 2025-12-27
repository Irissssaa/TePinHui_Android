package com.example.tepinhui.dto;

import java.util.List;

public class ProductListDTO {

    private List<ProductDTO> list;
    // 兼容不同后端分页字段命名（有的用 records / rows）
    private List<ProductDTO> records;
    private List<ProductDTO> rows;
    private int page;
    private int size;
    private int total;

    public List<ProductDTO> getList() {
        if (list != null) return list;
        if (records != null) return records;
        return rows;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getTotal() {
        return total;
    }
}
