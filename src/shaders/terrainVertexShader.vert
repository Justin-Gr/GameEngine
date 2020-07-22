#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 pass_surfaceNormal;
out vec3 pass_toLightVector;
out vec3 pass_toCameraVector;
out float pass_visibility;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform vec3 lightPosition;

const float density = 0.002;
const float gradient = 10.0;

void main(void) {

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	
	float distance = length(positionRelativeToCam.xyz);
	
	pass_textureCoords = textureCoords * 20;
	pass_surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	pass_toLightVector = lightPosition - worldPosition.xyz;
	pass_toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	pass_visibility = exp(-pow(distance * density, gradient));
	pass_visibility = clamp(pass_visibility, 0.0, 1.0);

}