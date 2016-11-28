#ifdef GL_ES
precision mediump float;
#endif

varying vec3 v_position;
varying vec3 v_normal;
varying vec2 v_texCoords;

void main() {
    gl_FragColor = vec4(v_texCoords.x, v_texCoords.y, 0.0, 1.0);
}