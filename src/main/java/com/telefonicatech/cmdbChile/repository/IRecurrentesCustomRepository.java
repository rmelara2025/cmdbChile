package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.dto.requestObject.RecurrenteRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.RecurrenteResponse;

import java.util.List;

public interface IRecurrentesCustomRepository {
    List<RecurrenteResponse> findCustomTotals(RecurrenteRequest req);
}
