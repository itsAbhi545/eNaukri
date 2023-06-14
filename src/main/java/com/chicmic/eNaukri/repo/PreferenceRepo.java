package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepo extends JpaRepository<Preference,Long> {
}
