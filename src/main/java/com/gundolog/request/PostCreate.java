package com.gundolog.request;

import javax.validation.constraints.NotBlank;

public class PostCreate {
    @NotBlank
    public String title;

    @NotBlank
    public String content;
}
