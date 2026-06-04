package com.welpeth.kakebo.financier.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Paged<E> {

  private List<E> items;

  private long totalItems;

}