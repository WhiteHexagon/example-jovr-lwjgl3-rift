package com.sunshineapps.riftexample;

import static com.oculusvr.capi.OvrLibrary.ovrDistortionCaps.ovrDistortionCap_Chromatic;
import static com.oculusvr.capi.OvrLibrary.ovrDistortionCaps.ovrDistortionCap_TimeWarp;
import static com.oculusvr.capi.OvrLibrary.ovrDistortionCaps.ovrDistortionCap_Vignette;
import static com.oculusvr.capi.OvrLibrary.ovrTrackingCaps.ovrTrackingCap_MagYawCorrection;
import static com.oculusvr.capi.OvrLibrary.ovrTrackingCaps.ovrTrackingCap_Orientation;
import static com.oculusvr.capi.OvrLibrary.ovrTrackingCaps.ovrTrackingCap_Position;
import static org.lwjgl.opengl.GL11.GL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_DECAL;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.GL_SPOT_CUTOFF;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightf;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glLoadMatrix;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.system.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.system.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.system.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.system.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.system.glfw.GLFW.glfwInit;
import static org.lwjgl.system.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.system.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.system.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.system.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.system.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.system.glfw.GLFW.glfwTerminate;
import static org.lwjgl.system.glfw.GLFW.glfwWindowShouldClose;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.glfw.ErrorCallback;
import org.lwjgl.system.glfw.GLFW;
import org.lwjgl.system.glfw.GLFWvidmode;
import org.lwjgl.system.glfw.WindowCallback;
import org.lwjgl.system.glfw.WindowCallbackAdapter;
import org.saintandreas.math.Matrix4f;
import org.saintandreas.math.Vector3f;

import com.oculusvr.capi.EyeRenderDesc;
import com.oculusvr.capi.FovPort;
import com.oculusvr.capi.GLTexture;
import com.oculusvr.capi.GLTextureData;
import com.oculusvr.capi.Hmd;
import com.oculusvr.capi.OvrLibrary;
import static com.oculusvr.capi.OvrLibrary.ovrEyeType.ovrEye_Left;
import static com.oculusvr.capi.OvrLibrary.ovrEyeType.ovrEye_Right;
import static com.oculusvr.capi.OvrLibrary.ovrEyeType.ovrEye_Count;
import com.oculusvr.capi.OvrRecti;
import com.oculusvr.capi.OvrSizei;
import com.oculusvr.capi.OvrVector2i;
import com.oculusvr.capi.OvrVector3f;
import com.oculusvr.capi.Posef;
import com.oculusvr.capi.RenderAPIConfig;
import com.oculusvr.capi.TextureHeader;
import com.sunshineapps.riftexample.thirdparty.FixedTexture;
import com.sunshineapps.riftexample.thirdparty.FixedTexture.BuiltinTexture;
import com.sunshineapps.riftexample.thirdparty.FrameBuffer;
import com.sunshineapps.riftexample.thirdparty.MatrixStack;
import com.sunshineapps.riftexample.thirdparty.RiftUtils;

public final class RiftClient0440 {
    private final boolean useDebugHMD = true;
    private final int RIFT_MONITOR = 0;        //This needs to be set manually since we cant detect which is the rift currently. try 0 or 1
    private long window;
    private long riftMonitorId;
    private final int riftWidth = 1920;         //DK2
    private final int riftHeight = 1080;        //DK2
    
    //OpenGL
    private final FloatBuffer projectionDFB[];
    private final FloatBuffer modelviewDFB;
    private FrameBuffer eyeDFB[];
    private FixedTexture cheq;
    
    // Rift Specific
    private Hmd hmd;
    private int frameCount = -1;
    private final OvrVector3f eyeOffsets[] = (OvrVector3f[]) new OvrVector3f().toArray(2);
    private final OvrRecti[] eyeRenderViewport = (OvrRecti[]) new OvrRecti().toArray(2);
    private final Posef poses[] = (Posef[]) new Posef().toArray(2);
    private final GLTexture eyeTextures[] = (GLTexture[]) new GLTexture().toArray(2);
    private final FovPort fovPorts[] = (FovPort[]) new FovPort().toArray(2);
    private final Matrix4f projections[] = new Matrix4f[2];
    private float ipd = OvrLibrary.OVR_DEFAULT_IPD;
    private float eyeHeight = OvrLibrary.OVR_DEFAULT_EYE_HEIGHT;
    
    // Scene
    private Matrix4f player;
    
    //FPS
    private final int fpsReportingPeriodSeconds = 5;
    private final ScheduledExecutorService fpsCounter = Executors.newSingleThreadScheduledExecutor();
    private final AtomicInteger frames = new AtomicInteger(0);
    private final AtomicInteger fps = new AtomicInteger(0);
    Runnable fpsJob = new Runnable() {
        public void run() {
            int frameCount = frames.getAndSet(0);
            fps.set(frameCount/fpsReportingPeriodSeconds);
            frames.addAndGet(frameCount-(fps.get()*fpsReportingPeriodSeconds));
            System.out.println(frameCount+" frames in "+fpsReportingPeriodSeconds+"s. "+fps.get()+"fps");
        }
    };
    
    public RiftClient0440() {
        System.out.println("RiftClient0440()");
        modelviewDFB =  ByteBuffer.allocateDirect(4*4*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        projectionDFB = new FloatBuffer[2];
        for (int eye = 0; eye < 2; ++eye) {
            projectionDFB[eye] = ByteBuffer.allocateDirect(4*4*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        }
    }
    
    public boolean findRift() {
        PointerBuffer monitors = GLFW.glfwGetMonitors();
        IntBuffer modeCount = BufferUtils.createIntBuffer(1);
        for (int i = 0; i < monitors.limit(); i++) {
            long monitorId = monitors.get(i);
//            System.out.println("monitor: " + monitorId);
            ByteBuffer modes = GLFW.glfwGetVideoModes(monitorId, modeCount);
            System.out.println("mode count=" + modeCount.get(0));
            for (int j = 0; j < modeCount.get(0); j++) {
                modes.position(j * GLFWvidmode.SIZEOF);

                int width = GLFWvidmode.width(modes);
                int height = GLFWvidmode.height(modes);
                // System.out.println(width + "," + height + "," + monitorId);

                if (width == riftWidth && height == riftHeight) {
                    System.out.println("found dimensions match: " + width + "," + height + "," + monitorId);
                    riftMonitorId = monitorId;
                    if (i == RIFT_MONITOR) {
                        return true;
                    }
                }
            }
            System.out.println("-----------------");
        }
        return (riftMonitorId != 0);
    }  

    private void recenterView() {
        Vector3f center = Vector3f.UNIT_Y.mult(eyeHeight);
        // Vector3f eye = new Vector3f(0, eyeHeight, ipd * 10.0f); //jherico
        Vector3f eye = new Vector3f(ipd * 5.0f, eyeHeight, 0.0f);
        player = Matrix4f.lookat(eye, center, Vector3f.UNIT_Y).invert();
        // worldToCamera = Matrix4f.lookat(eye, center, Vector3f.UNIT_Y);
        hmd.recenterPose();
    }
    
    public final void drawPlaneXZ() {
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
        float roomSize = 4.0f;
        float tileSize = 4.0f; // if same then there are two tiles per square
        glBegin(GL_QUADS);
            glNormal3f(1f, 0f, 1f);
            glColor4f(1f, 1f, 1f, 1f);
            glTexCoord2f(0f, 0f);
            glVertex3f(-roomSize, 0f, -roomSize);
            glTexCoord2f(tileSize, 0f);
            glVertex3f(roomSize, 0f, -roomSize);
            glTexCoord2f(tileSize, tileSize);
            glVertex3f(roomSize, 0f, roomSize);
            glTexCoord2f(0f, tileSize);
            glVertex3f(-roomSize, 0f, roomSize);
        glEnd();
    }
    
    public void run() {
        System.out.println(""+System.getProperty("java.version"));
        
        // step 1 - hmd init
        System.out.println("step 1 - hmd init");
        Hmd.initialize();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        // step 2 - hmd create
        System.out.println("step 2 - hmd create");
        hmd = Hmd.create(0); // assume 1 device at index 0
        if (hmd == null) {
            System.out.println("null hmd");
            hmd = Hmd.createDebug(OvrLibrary.ovrHmdType.ovrHmd_DK2);
            if (!useDebugHMD) {
                return;
            }
        }
        
        // step 3 - hmd size queries
        System.out.println("step 3 - hmd sizes");
        OvrSizei resolution = hmd.Resolution;
        System.out.println("resolution= " + resolution.w + "x" + resolution.h);

        OvrSizei recommendedTex0Size = hmd.getFovTextureSize(ovrEye_Left, hmd.DefaultEyeFov[ovrEye_Left], 1.0f);
        OvrSizei recommendedTex1Size = hmd.getFovTextureSize(ovrEye_Right, hmd.DefaultEyeFov[ovrEye_Right], 1.0f);
        System.out.println("left= " + recommendedTex0Size.w + "x" + recommendedTex0Size.h);
        System.out.println("right= " + recommendedTex1Size.w + "x" + recommendedTex1Size.h);
        int displayW = recommendedTex0Size.w + recommendedTex1Size.w;
        int displayH = Math.max(recommendedTex0Size.h, recommendedTex1Size.h);
        OvrSizei renderTargetEyeSize = new OvrSizei(displayW / 2, displayH);    //single eye
        System.out.println("using eye size " + renderTargetEyeSize.w + "x" + renderTargetEyeSize.h);

        eyeRenderViewport[ovrEye_Left].Pos = new OvrVector2i(0, 0);
        eyeRenderViewport[ovrEye_Left].Size = renderTargetEyeSize;
        eyeRenderViewport[ovrEye_Right].Pos = eyeRenderViewport[ovrEye_Left].Pos;
        eyeRenderViewport[ovrEye_Right].Size = renderTargetEyeSize;

        eyeTextures[ovrEye_Left].ogl = new GLTextureData(new TextureHeader(renderTargetEyeSize, eyeRenderViewport[ovrEye_Left]));
        eyeTextures[ovrEye_Right].ogl = new GLTextureData(new TextureHeader(renderTargetEyeSize, eyeRenderViewport[ovrEye_Right]));

        // step 4 - tracking
        System.out.println("step 4 - tracking");
        if (hmd.configureTracking(ovrTrackingCap_Orientation | ovrTrackingCap_MagYawCorrection | ovrTrackingCap_Position, 0) == 0) {
            throw new IllegalStateException("Unable to start the sensor");
        }

        // step 5 - FOV
        System.out.println("step 5 - FOV");
        for (int eye = 0; eye < ovrEye_Count; ++eye) {
            fovPorts[eye] = hmd.DefaultEyeFov[eye];
            projections[eye] = RiftUtils.toMatrix4f(Hmd.getPerspectiveProjection(fovPorts[eye], 0.1f, 1000000f, true));
        }

        // step 6 - player params
        System.out.println("step 6 - player params");
        ipd = hmd.getFloat(OvrLibrary.OVR_KEY_IPD, ipd);
        eyeHeight = hmd.getFloat(OvrLibrary.OVR_KEY_EYE_HEIGHT, eyeHeight);
        recenterView();
        System.out.println("eyeheight=" + eyeHeight + " ipd=" + ipd);

        System.out.println("Hello LWJGL " + Sys.getVersion() + "!");
        try {
            init();
            loop();
            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            fpsCounter.shutdown();
        }
    }

    private void init() {
        // step 7 - opengl window
        System.out.println("step 7 - window");
        
        glfwSetErrorCallback(ErrorCallback.Util.getDefault());
        if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        // glfwDefaultWindowHints();    //not needed
        if (findRift()) {
            window = glfwCreateWindow(riftWidth, riftHeight, "Hello World!", riftMonitorId, NULL);          //where is this text used?
            System.out.println("found rift and using it " + riftMonitorId);
        } else {
            window = glfwCreateWindow(riftWidth, riftHeight, "Hello World!", NULL, NULL);
            System.out.println("use rift debug mode");
        }
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        WindowCallback.set(window, new WindowCallbackAdapter() {
            @Override
            public void key(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, GL_TRUE);
                }
                if (key == GLFW_KEY_R && action == GLFW_RELEASE) {
                    recenterView();
                }
            }
        });
        glfwMakeContextCurrent(window);
 //       glfwSwapInterval(1);              //not needed?
        glfwShowWindow(window);
        
        GLContext.createFromCurrent();
        glClearColor(.42f, .67f, .87f, 1f);
        
        // Lighting
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_COLOR_MATERIAL);

        FloatBuffer lightPos = ByteBuffer.allocateDirect(4*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        lightPos.put(new float[]{0.5f, 0.0f, 1.0f, 0.0001f});
        lightPos.rewind();
        
        FloatBuffer noAmbient = ByteBuffer.allocateDirect(4*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        noAmbient.put(new float[]{0.2f, 0.2f, 0.2f, 1.0f});
        noAmbient.rewind();
        
        FloatBuffer diffuse = ByteBuffer.allocateDirect(4*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        diffuse.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        diffuse.rewind();
        
        FloatBuffer spec = ByteBuffer.allocateDirect(4*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        spec.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        spec.rewind();
        
        glLight(GL_LIGHT0, GL_AMBIENT, noAmbient);
        glLight(GL_LIGHT0, GL_SPECULAR, spec);
        glLight(GL_LIGHT0, GL_DIFFUSE, diffuse);
        glLight(GL_LIGHT0, GL_POSITION, lightPos);
        glLightf(GL_LIGHT0, GL_SPOT_CUTOFF, 45.0f);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        RenderAPIConfig rc = new RenderAPIConfig();
        rc.Header.BackBufferSize = hmd.Resolution;
        rc.Header.Multisample = 1;
        int distortionCaps = ovrDistortionCap_Chromatic | ovrDistortionCap_TimeWarp | ovrDistortionCap_Vignette;
        EyeRenderDesc eyeRenderDescs[] = hmd.configureRendering(rc, distortionCaps, fovPorts);
        for (int eye = 0; eye < 2; ++eye) {
            eyeOffsets[eye].x = eyeRenderDescs[eye].HmdToEyeViewOffset.x;
            eyeOffsets[eye].y = eyeRenderDescs[eye].HmdToEyeViewOffset.y;
            eyeOffsets[eye].z = eyeRenderDescs[eye].HmdToEyeViewOffset.z;
        }

        eyeDFB = new FrameBuffer[2];
        eyeDFB[ovrEye_Left] = new FrameBuffer(eyeRenderViewport[ovrEye_Left].Size.w, eyeRenderViewport[ovrEye_Left].Size.h);
        eyeDFB[ovrEye_Right] = new FrameBuffer(eyeRenderViewport[ovrEye_Right].Size.w, eyeRenderViewport[ovrEye_Right].Size.h);

        eyeTextures[ovrEye_Left].ogl.TexId = eyeDFB[ovrEye_Left].getTexture().id;
        eyeTextures[ovrEye_Right].ogl.TexId = eyeDFB[ovrEye_Right].getTexture().id;

        // scene prep
        glEnable(GL_TEXTURE_2D);
        cheq = FixedTexture.createBuiltinTexture(BuiltinTexture.tex_checker);
        glDisable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
        
        // initial matrix stuff
        glMatrixMode(GL_PROJECTION);
        for (int eye = 0; eye < ovrEye_Count; ++eye) {
            MatrixStack.PROJECTION.set(projections[eye]);
            glMatrixMode(GL_PROJECTION);
            MatrixStack.PROJECTION.top().fillFloatBuffer(projectionDFB[eye], true);
            projectionDFB[eye].rewind();
            glLoadMatrix(projectionDFB[eye]);
        }

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        recenterView();
        MatrixStack.MODELVIEW.set(player.invert());

        modelviewDFB.clear();
        MatrixStack.MODELVIEW.top().fillFloatBuffer(modelviewDFB, true);
        modelviewDFB.rewind();
        glLoadMatrix(modelviewDFB);

        //fps
        fpsCounter.scheduleAtFixedRate(fpsJob, 0, fpsReportingPeriodSeconds, TimeUnit.SECONDS); 
    }

    private void loop() {
        // step 8 - animator loop
        System.out.println("step 8 - animator loop");
        while (glfwWindowShouldClose(window) == GL_FALSE) {
            hmd.beginFrameTiming(++frameCount);
            Posef eyePoses[] = hmd.getEyePoses(frameCount, eyeOffsets);
            for (int eyeIndex = 0; eyeIndex < ovrEye_Count; eyeIndex++) {
                int eye = hmd.EyeRenderOrder[eyeIndex];
                Posef pose = eyePoses[eye];
                poses[eye].Orientation = pose.Orientation;
                poses[eye].Position = pose.Position;

                eyeDFB[eye].activate();
                
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                glMatrixMode(GL_PROJECTION);
                glLoadMatrix(projectionDFB[eye]);

                glMatrixMode(GL_MODELVIEW);
                MatrixStack mv = MatrixStack.MODELVIEW;
                mv.push();
                {
                    mv.preTranslate(RiftUtils.toVector3f(poses[eye].Position).mult(-1));
                    mv.preRotate(RiftUtils.toQuaternion(poses[eye].Orientation).inverse());
                    mv.translate(new Vector3f(0, eyeHeight, 0));
                    modelviewDFB.clear();
                    MatrixStack.MODELVIEW.top().fillFloatBuffer(modelviewDFB, true);
                    modelviewDFB.rewind();
                    glLoadMatrix(modelviewDFB);

                    // tiles on floor
                    glEnable(GL_TEXTURE_2D);
                    glBindTexture(GL_TEXTURE_2D, cheq.getId());
                    glTranslatef(0.0f, -eyeHeight, 0.0f);
                    drawPlaneXZ();
                    glTranslatef(0.0f, eyeHeight, 0.0f);
                    glDisable(GL_TEXTURE_2D);
                }
                mv.pop();
            }
            ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
            glBindTexture(GL_TEXTURE_2D, 0);
            glDisable(GL_TEXTURE_2D);
            glfwPollEvents();

            frames.incrementAndGet();
            hmd.endFrame(poses, eyeTextures);
        }
    }

    public static void main(String[] args) {
        new RiftClient0440().run();
    }
}