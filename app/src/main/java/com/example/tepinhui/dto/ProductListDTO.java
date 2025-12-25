package com.example.tepinhui.dto;

import java.util.List;

public class ProductListDTO {

    private List<ProductDTO> list;
    private int page;
    private int size;
    private int total;

    public List<ProductDTO> getList() {
        return list;
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
