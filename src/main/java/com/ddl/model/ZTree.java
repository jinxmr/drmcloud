package com.ddl.model;

import lombok.Data;

@Data
public class ZTree {

    private Integer id;

    private Integer pId;

    private String name;

    private boolean checked;

    private boolean open;

}
