package com.shopme.admin.brand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@ResponseStatus(code=HttpStatus.NOT_FOUND, reason="Brand not found")
public class BrandNotFoundRestException extends Exception {

}
