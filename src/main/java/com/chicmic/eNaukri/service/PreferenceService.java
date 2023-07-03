package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.repo.PreferenceRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreferenceService {
    private final UsersRepo usersRepo;
    private final PreferenceRepo preferenceRepo;

}
