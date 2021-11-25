package mattwalsh.hebtest.rest;

import java.util.List;
import java.util.UUID;

public record ImageResponse(
        UUID id,
        String imageDataChecksum,
        String label,
        List<String> detectedObjects
) { }
