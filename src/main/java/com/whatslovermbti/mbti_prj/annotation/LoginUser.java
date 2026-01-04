package com.whatslovermbti.mbti_prj.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 컨트롤러에서 user id값만 필요할 때 사용
public @interface LoginUser {
}