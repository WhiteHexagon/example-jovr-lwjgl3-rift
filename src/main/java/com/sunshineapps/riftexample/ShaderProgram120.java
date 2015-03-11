package com.sunshineapps.riftexample;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.system.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.system.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.system.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.system.glfw.GLFW.glfwInit;
import static org.lwjgl.system.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.system.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.system.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.system.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.system.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.system.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.system.glfw.GLFW.glfwTerminate;
import static org.lwjgl.system.glfw.GLFW.glfwWindowShouldClose;

import java.util.concurrent.atomic.AtomicBoolean;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.glfw.ErrorCallback;
import org.lwjgl.system.glfw.WindowCallback;
import org.lwjgl.system.glfw.WindowCallbackAdapter;

public final class ShaderProgram120 {
    private final AtomicBoolean shouldShutdon = new AtomicBoolean(false);
    private int program;

    public void init() {
        // vertex shader
        final int vertShader = ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
        if (vertShader == GL_FALSE) {
            throw new RuntimeException("failed to create vert shader");
        }
        final String vertexCode = ""
                + "#version 120                                                 \n"
                + "                                                             \n" 
                + "                                                             \n" 
                + "void main() {                                                \n" 
                //+ "    gl_Position = gl_Vertex;                                 \n"
                + "    gl_Position = vec4(gl_Vertex.xyz * 0.5, gl_Vertex.w);    \n"
                + "}                                                            \n";
        ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
        ARBShaderObjects.glCompileShaderARB(vertShader);
        if (GL20.glGetShaderi(vertShader, GL20.GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(vertShader));
            throw new RuntimeException("vert comp failed");
        }

        // fragment shader
        final int fragShader = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        if (fragShader == GL_FALSE) {
            throw new RuntimeException("failed to create frag shader");
        }
        final String fragCode = ""
                + "#version 120                                     \n"
                + "                                                 \n" 
                + "void main() {                                    \n" 
                + "    gl_FragColor = vec4(1.0, 1.0, 0.4, 1.0);     \n"
                + "}                                                \n";
        ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
        ARBShaderObjects.glCompileShaderARB(fragShader);
        if (GL20.glGetShaderi(fragShader, GL20.GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(fragShader));
            throw new RuntimeException("frag comp failed");
        }

        // program
        program = ARBShaderObjects.glCreateProgramObjectARB();
        if (program == GL_FALSE) {
            throw new RuntimeException("failed to create shader program");
        }
        ARBShaderObjects.glAttachObjectARB(program, vertShader);
        ARBShaderObjects.glAttachObjectARB(program, fragShader);
        ARBShaderObjects.glLinkProgramARB(program);                     //is there a way to check this result?
        ARBShaderObjects.glValidateProgramARB(program);                 //is there a way to check this result?

        glClearColor(.42f, .67f, .87f, 1f);
    }

    public void renderScene() {
        glClear(GL11.GL_COLOR_BUFFER_BIT);

        ARBShaderObjects.glUseProgramObjectARB(program);
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3f(-1.0f, 1.0f, 0.0f);
            GL11.glVertex3f(1.0f, 1.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
        GL11.glEnd();
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    private void start() {
        final int windowWidth = 1024;
        final int windowHeight = 900;
        glfwSetErrorCallback(ErrorCallback.Util.getDefault());
        if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        long window = glfwCreateWindow(windowWidth, windowHeight, "Hello World!", NULL, NULL);
        WindowCallback.set(window, new WindowCallbackAdapter() {
            @Override
            public void key(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    shouldShutdon.set(true);
                }
            }
        });
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        try {
            GLContext.createFromCurrent();
            glViewport(0, 0, windowWidth, windowHeight);
            init();
            while (shouldShutdon.get() == false) {
                renderScene();
                glfwSwapBuffers(window);
                glfwPollEvents();
                if (glfwWindowShouldClose(window) == GL_TRUE) {
                    shouldShutdon.set(true);
                }
            }
        } finally {
            glfwSetWindowShouldClose(window, GL_TRUE);
            glfwDestroyWindow(window);
            glfwTerminate();
        }
    }

    public static void main(String[] args) {
        new ShaderProgram120().start();
    }
}
