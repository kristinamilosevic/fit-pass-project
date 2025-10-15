//package rs.ac.uns.ftn.svt.controller.es;
//
//import org.springframework.web.bind.annotation.*;
//import rs.ac.uns.ftn.svt.model.es.FacilityES;
//import rs.ac.uns.ftn.svt.service.es.FacilityESService;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
//
//@RestController
//@RequestMapping("/api/es/facilities")
//public class FacilityESController {
//
//    private final FacilityESService service;
//
//    public FacilityESController(FacilityESService service) {
//        this.service = service;
//    }
//
//    @GetMapping
//    public List<FacilityES> getAll() {
//        return StreamSupport.stream(service.findAll().spliterator(), false)
//                .collect(Collectors.toList());
//    }
//
//    @GetMapping("/search")
//    public List<FacilityES> search(@RequestParam("q") String query) {
//        // za sada jednostavna full scan pretraga (kasnije možemo koristiti query DSL)
//        return StreamSupport.stream(service.findAll().spliterator(), false)
//                .filter(f -> f.getName().toLowerCase().contains(query.toLowerCase())
//                        || f.getCity().toLowerCase().contains(query.toLowerCase())
//                        || f.getDisciplines().toLowerCase().contains(query.toLowerCase()))
//                .collect(Collectors.toList());
//    }
//}
