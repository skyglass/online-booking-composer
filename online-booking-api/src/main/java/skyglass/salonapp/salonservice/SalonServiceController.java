package skyglass.salonapp.salonservice;


import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:3000")
public class SalonServiceController {

    SalonService salonService;

    public  SalonServiceController(SalonService salonService){
        this.salonService=salonService;
    }

    @GetMapping("/retrieveAvailableSalonServices")
    @ApiOperation(value = "RetrieveAvailableSalonServicesAPI")
    public List<SalonServiceDetail> retrieveAvailableSalonServicesAPI(){

        return salonService.findAll();
    }
}
