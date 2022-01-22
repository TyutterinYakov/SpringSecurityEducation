package test.security.securitytest.rest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import test.security.securitytest.model.Developer;

@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperRestController {
	
	private List<Developer> developers = Stream.of(
			new Developer(1L, "Максим", "Макаров"),
			new Developer(2L, "Артем", "Ефимов"),
			new Developer(3L, "Сергей", "Петров")
			
			).collect(Collectors.toList());
	
	@GetMapping
	public List<Developer> getAll(){
		return developers;
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('developers:read')")
	public Developer getById(@PathVariable Long id) {
		
		return developers.stream().filter(developer->developer.getId().equals(id)).findFirst().orElse(null);
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('developers:write')")
	public Developer create(@RequestBody Developer developer) {
		this.developers.add(developer);
		return developer;
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('developers:write')")
	public void deleteById(@PathVariable("id") Long id) {
		this.developers.removeIf(dev->dev.getId().equals(id));
	}
}
