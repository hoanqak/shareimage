package com.service;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

@Service
public class DozerService
{
    public DozerBeanMapper getDozer(){
        return new DozerBeanMapper();
    }
}
