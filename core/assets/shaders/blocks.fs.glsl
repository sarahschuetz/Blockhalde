#ifdef GL_ES
precision mediump float;
#endif

varying vec3 v_position;
varying vec3 v_normal;
varying vec2 v_texCoords;
varying vec3 v_eyeVec;

uniform sampler2D u_texture;

void main() {

	v_normal = normalize(v_normal);
	v_eyeVec = normalize(v_eyeVec);

	float diffuse = max(dot(v_normal,v_eyeVec),0.3);
	diffuse = min(diffuse,1.0);
	
	vec3 reflectVec = reflect(-v_eyeVec,v_normal);
	float spec = pow( max( dot(reflectVec, v_eyeVec), 0.0), 2);
	vec4 c_spec = clamp(spec * vec4(0.5,0.5,0.5,1.0), 0.0, 1.0);

    gl_FragColor = texture2D(u_texture, v_texCoords) * diffuse + c_spec; // vec4(v_texCoords.x, v_texCoords.y, 0.0, 1.0)
}
