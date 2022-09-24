package com.github.quaoz.util;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageComponents;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;
import io.github.cdimascio.dotenv.Dotenv;
import org.jetbrains.annotations.NotNull;

public class Geocoder {
	private static final JOpenCageGeocoder jOpenCageGeocoder;

	static {
		Dotenv dotenv = Dotenv.load();
		String key = dotenv.get("OPEN_CAGE_API_KEY");

		jOpenCageGeocoder = new JOpenCageGeocoder(key);
	}

	public static String standardise(double latitude, double longitude) {
		JOpenCageReverseRequest request = new JOpenCageReverseRequest(latitude, longitude);
		request.setMinConfidence(1);
		request.setNoAnnotations(false);

		return standardise(jOpenCageGeocoder.reverse(request));
	}

	public static String standardise(@NotNull String location) {
		JOpenCageForwardRequest request = new JOpenCageForwardRequest(location);
		request.setMinConfidence(1);
		request.setNoAnnotations(false);

		return standardise(jOpenCageGeocoder.forward(request));
	}

	private static String standardise(@NotNull JOpenCageResponse results) {
		results.orderResultByConfidence();

		JOpenCageComponents components = results.getFirstComponents();
		return String.format(
				"%s, %s, %s, %s, %s",
				components.getCountry(),
				components.getState(),
				components.getStateDistrict(),
				components.getCounty(),
				components.getCity());
	}
}
