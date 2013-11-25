package shader;

import java.io.BufferedReader;
import java.io.FileReader;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

public class InitShader {
       
        public static String readShaderSource(String filename){
                String buffer = "";
                BufferedReader ifile;
               
                try {
                        ifile = new BufferedReader(new FileReader(filename));
                        String line;
                        while ((line = ifile.readLine()) != null) {
                                buffer += line + "\n";
                        }
                        ifile.close();
                       
                } catch (Exception e){
                        System.err.println(e.getMessage());
                        return null;
                }
                return buffer;
        }
       
        public static int initProgram(String vShaderFile, String fShaderFile) {
               
                class Shader{
                        String filename;
                        int type;
                        String source;
                }
               
                Shader[] shaders = new Shader[2];
               
                for (int i = 0; i < shaders.length; i++){
                        shaders[i] = new Shader();
                }
               
                shaders[0].filename = vShaderFile;
                shaders[0].type = ARBVertexShader.GL_VERTEX_SHADER_ARB;
               
                shaders[1].filename = fShaderFile;
                shaders[1].type = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
               
                //create a program object to which both shaders will get attached
                int programObj = ARBShaderObjects.glCreateProgramObjectARB();
               
                if (programObj == 0){
                        System.err.println("Error creating container object for shader");
                        return -1;
                }
               
                // compile and attach shader
                for (int i = 0; i < shaders.length; i++){
                        Shader s = shaders[i];
                        s.source = readShaderSource(s.filename);
                       
                        if (s.source == null){
                                System.err.println("Error reading file \"" + "\"!");
                                return -1;
                        }
                        int shader = ARBShaderObjects.glCreateShaderObjectARB(s.type);
                        ARBShaderObjects.glShaderSourceARB(shader, s.source);
                       
                        ARBShaderObjects.glCompileShaderARB(shader);
                       
                        try{
                                if (ARBShaderObjects.glGetObjectParameteriARB(shader,
                                                ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                                        throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
                        } catch(Exception e){
                                ARBShaderObjects.glDeleteObjectARB(shader);
                                System.err.println(e.getMessage());
                                return -1;
                        }
                        ARBShaderObjects.glAttachObjectARB(programObj, shader);
                }
                //Link program
                ARBShaderObjects.glLinkProgramARB(programObj);
               
                if (ARBShaderObjects.glGetObjectParameteriARB(programObj,
                                ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE){
                        System.err.println(getLogInfo(programObj));
                        return -1;
                }
                return programObj;
        }
       
        private static String getLogInfo(int shader){
                return ARBShaderObjects.glGetInfoLogARB(shader,
                                ARBShaderObjects.glGetObjectParameteriARB(shader,
                                                ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
        }
}