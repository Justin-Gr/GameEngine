#version 400 core

in vec2 pass_textureCoords;
in vec3 pass_surfaceNormal;
in vec3 pass_toLightVector;
in vec3 pass_toCameraVector;
in float pass_visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {

	// PER-PIXEL LIGHTING

	vec3 unitSurfaceNormal = normalize(pass_surfaceNormal);			// vecteurs unitaires
	vec3 unitToLightVector = normalize(pass_toLightVector);
	
	float brightness = dot(unitSurfaceNormal, unitToLightVector);	// produit scalaire <normale, lumi�re>
	brightness = max(brightness, 0.2);
	vec3 diffuse = brightness * lightColor;							// coloration de la lumi�re
	
	// SPECULAR LIGHTING
	
	vec3 unitToCameraVector = normalize(pass_toCameraVector);							// vecteurs unitaires
	vec3 unitFromLightVector = -unitToLightVector;
	vec3 reflectedFromLightVector = reflect(unitFromLightVector, unitSurfaceNormal);	// r�flexion de la lumi�re sur la surface
	
	float specularFactor = dot(reflectedFromLightVector, unitToCameraVector);			// produit scalaire <lumi�re r�flechie, direction camera>
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper); 								// sensibilit� de l'effet de r�flexion
	vec3 finalSpecular = reflectivity * dampedFactor * lightColor; 						// coloration de la lumi�re et intensit� de la r�flexion

	// FINAL COLOR

	vec4 textureColor = texture(textureSampler, pass_textureCoords);	// gestion de la transparence
	if (textureColor.a < 0.5) {
		discard;
	}

	out_Color = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
	out_Color = mix(vec4(skyColor, 1.0), out_Color, pass_visibility);			// effet de brouillard

}
