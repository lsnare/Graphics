#version 150

in vec4 vPosition;
in vec4 vColor;
out vec4 fColor;

uniform vec3 theta;

void 
main()
{
	mat3 rotx = mat3(1.0, 0.0,          0.0,
					 0.0, cos(theta.x), -sin(theta.x),
					 0.0, sin(theta.x), cos(theta.x)
					);
	
	mat3 roty = mat3(cos(theta.y), 0.0, -sin(theta.y),
					 0.0, 		   1.0, 0.0,
					 sin(theta.y), 0.0, cos(theta.y)
					);
	
	mat3 rotz = mat3(cos(theta.z),  sin(theta.z), 0.0,
					 -sin(theta.z), cos(theta.z), 0.0,
					 0.0, 			0.0, 		  1.0
					);
	
	mat3 rot = mat3(rotx * roty * rotz);
	
	gl_Position = vec4(rot * vPosition.xyz, 1.0);
	fColor = vec4(vColor);
} 