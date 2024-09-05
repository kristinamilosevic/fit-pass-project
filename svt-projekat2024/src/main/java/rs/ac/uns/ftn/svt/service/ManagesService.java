package rs.ac.uns.ftn.svt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.model.Manages;
import rs.ac.uns.ftn.svt.repository.ManagesRepository;

@Service
public class ManagesService {

    @Autowired
    private ManagesRepository managesRepository;

    public void save(Manages manages) {
        managesRepository.save(manages);
    }
}
