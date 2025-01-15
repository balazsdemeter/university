package hu.cubix.university.web;

import hu.cubix.university.api.ImageControllerApi;
import hu.cubix.university.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ImageController implements ImageControllerApi {

	private final ImageRepository imageRepository;

	@Override
	public Optional<NativeWebRequest> getRequest() {
		return ImageControllerApi.super.getRequest();
	}

	@Override
	public ResponseEntity<Resource> downloadImage(Long id) {
		return ResponseEntity.ok(
				new ByteArrayResource(
						imageRepository.findById(id).get().getData()
						)
				);
	}
}