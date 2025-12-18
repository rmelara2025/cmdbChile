package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.dto.RecurrenteRequest;
import com.telefonicatech.cmdbChile.dto.RecurrenteResponse;

import java.util.List;

public interface IRecurrentesCustomRepository {
    List<RecurrenteResponse> findCustomTotals(RecurrenteRequest req);
}
