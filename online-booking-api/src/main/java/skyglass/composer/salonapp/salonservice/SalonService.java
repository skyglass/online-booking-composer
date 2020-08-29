package skyglass.composer.salonapp.salonservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SalonService {



    SalonServiceDetailRepository salonServiceDetailRepository;


    public  SalonService(SalonServiceDetailRepository salonServiceDetailRepository){
        this.salonServiceDetailRepository=salonServiceDetailRepository;
    }


    public List<SalonServiceDetail> findAll() {

        return salonServiceDetailRepository.findAll();
    }

    public Optional<SalonServiceDetail> findById(Long id){
        return salonServiceDetailRepository.findById(id);
    }

}
