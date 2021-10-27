package com.pekon.kotlin.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

/**
 * @hide
 */
@IntDef(value = {
        AnnoType.a,
        AnnoType.b,
        AnnoType.c,
})
@Retention(RetentionPolicy.SOURCE)
public @interface DataAnnotation {
}
