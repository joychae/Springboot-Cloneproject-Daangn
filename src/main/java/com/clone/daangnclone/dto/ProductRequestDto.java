package com.clone.daangnclone.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class ProductRequestDto {

    private Long id;

    private String img;

    private String nickname;

    private String region;

    private String title;

    private String category;

    private String createdAt;

    private String price;

    private String contents;

    private int chat;

    private  int like;

    private int view;

}
