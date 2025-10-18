package com.uniClub.common.logging;

import com.uniClub.common.utils.OperationType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target({METHOD})//sadece methodların üstüne koyulur
@Retention(RetentionPolicy.RUNTIME)//çalışma zmaanında okunur
public @interface LoggableOperation {
    OperationType value();//zorunlu deger
}
//bu aop tabanlı bir loglama sistemi için olusturulmus bir anatasyondur
//api isteklerimizi yapacagımız methodların üstüne yazarız
//asıl iş LoggingAspect te dönüyor