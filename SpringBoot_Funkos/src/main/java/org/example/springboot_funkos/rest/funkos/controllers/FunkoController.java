package org.example.springboot_funkos.rest.funkos.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.springboot_funkos.rest.funkos.dto.FunkoDto;
import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.example.springboot_funkos.rest.funkos.services.FunkoService;
import org.example.springboot_funkos.utils.pagination.PageResponse;
import org.example.springboot_funkos.utils.pagination.PaginationLinksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/funkos")
public class FunkoController {

    private final FunkoService service;
    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public FunkoController(PaginationLinksUtils paginationLinksUtils, FunkoService funkoService) {
        this.service = funkoService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    @GetMapping()
    public ResponseEntity<PageResponse<Funko>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<Funko> pageResult = service.getAll(pageable);
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Funko> getById(@PathVariable String id) {  // Cambié Long por String para MongoDB
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Funko> getByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(service.getByNombre(nombre));
    }

    @GetMapping("/stock/{stock}")
    public ResponseEntity<Optional<Funko>> getByStock(@PathVariable Integer stock) {
        return ResponseEntity.ok(service.getByStock(stock));
    }

    @PostMapping
    public ResponseEntity<Funko> save(@Valid @RequestBody FunkoDto funkoDto) {
        var res = service.save(funkoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funko> update(@PathVariable String id, @Valid @RequestBody FunkoDto funkoDto) {  // Cambié Long por String para MongoDB
        if (funkoDto.getStock() == null) {
            funkoDto.setStock(0);
        }

        Funko updatedFunko = service.update(id, funkoDto);

        return ResponseEntity.ok(updatedFunko);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Funko> delete(@PathVariable String id) {  // Cambié Long por String para MongoDB
        var res = service.delete(id);
        return ResponseEntity.ok(res);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
