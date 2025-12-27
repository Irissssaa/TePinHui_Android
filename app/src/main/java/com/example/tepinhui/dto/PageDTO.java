package com.example.tepinhui.dto;

import java.util.List;

public class PageDTO<T> {
    public List<T> list;
    public int page;
    public int size;
    public int total;
}