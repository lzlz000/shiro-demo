package lzlz000.shirodemo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonMessage implements Serializable {
    String text;
    String errmessage;
    Serializable data;
}
