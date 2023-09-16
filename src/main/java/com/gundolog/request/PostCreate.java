package com.gundolog.request;

import javax.validation.constraints.NotBlank;

public class PostCreate {
    @NotBlank(message = "제목을 입력해주세요.")
    public String title;

    @NotBlank(message = "내용을 입력해주세요.")
    public String content;
}
