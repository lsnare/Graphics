#version 150

in vec4 vPosition;
in vec4 vColor;
out vec4 fColor;

void 
main()
{
	gl_Position = vec4(vPosition);
	fColor = vec4(vColor);
} 