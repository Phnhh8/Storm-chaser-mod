
package dev.stormchasers.client.vehicle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class ChaserTruckModel {

    private static final Identifier BLACK = id("block/black_concrete");
    private static final Identifier GRAY = id("block/light_gray_concrete");
    private static final Identifier YELLOW = id("block/yellow_concrete");
    private static final Identifier IRON = id("block/iron_block");
    private static final Identifier GLASS = id("block/light_blue_stained_glass");
    private static final Identifier LIGHT = id("block/sea_lantern");
    private static final Identifier ORANGE = id("block/orange_concrete");
    private static final Identifier DARK = id("block/deepslate_tiles");

    private ChaserTruckModel() {}

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath("minecraft", path);
    }

    public static void render(
        SubmitNodeCollector collector,
        PoseStack pose,
        int light,
        float wheelSpin,
        float radarSpin
    ) {

        // =====================================
        // LOWER CHASSIS
        // =====================================

        box(collector, pose, BLACK, light,
            -0.88F, 0.42F, -1.70F,
             0.88F, 0.83F,  1.65F
        );

        // Reinforced underbody
        box(collector, pose, IRON, light,
            -0.76F, 0.34F, -1.48F,
             0.76F, 0.43F,  1.48F
        );

        // Front bumper supports
        box(collector, pose, DARK, light,
            -0.96F, 0.43F, 1.48F,
            -0.76F, 0.66F, 1.78F
        );

        box(collector, pose, DARK, light,
             0.76F, 0.43F, 1.48F,
             0.96F, 0.66F, 1.78F
        );

        // =====================================
        // FRONT HOOD
        // =====================================

        box(collector, pose, BLACK, light,
            -0.85F, 0.77F, 0.48F,
             0.85F, 1.15F, 1.58F
        );

        box(collector, pose, GRAY, light,
            -0.76F, 1.13F, 0.65F,
             0.76F, 1.18F, 1.42F
        );

        // Yellow hood stripes
        box(collector, pose, YELLOW, light,
            -0.67F, 1.19F, 0.77F,
            -0.48F, 1.21F, 1.38F
        );

        box(collector, pose, YELLOW, light,
             0.48F, 1.19F, 0.77F,
             0.67F, 1.21F, 1.38F
        );

        // Hood center reinforcement
        box(collector, pose, DARK, light,
            -0.13F, 1.19F, 0.83F,
             0.13F, 1.23F, 1.40F
        );

        // =====================================
        // CABIN
        // =====================================

        // Main cabin frame
        box(collector, pose, BLACK, light,
            -0.79F, 0.85F, -0.76F,
             0.79F, 1.83F,  0.55F
        );

        // Roof
        box(collector, pose, BLACK, light,
            -0.84F, 1.78F, -0.82F,
             0.84F, 1.91F,  0.62F
        );

        // Front windshield
        box(collector, pose, GLASS, light,
            -0.65F, 1.24F, 0.56F,
             0.65F, 1.69F, 0.61F
        );

        // Rear windshield
        box(collector, pose, GLASS, light,
            -0.61F, 1.25F, -0.83F,
             0.61F, 1.65F, -0.78F
        );

        // Left side window
        box(collector, pose, GLASS, light,
            -0.81F, 1.25F, -0.57F,
            -0.77F, 1.67F,  0.37F
        );

        // Right side window
        box(collector, pose, GLASS, light,
             0.77F, 1.25F, -0.57F,
             0.81F, 1.67F,  0.37F
        );

        // Window pillars
        box(collector, pose, YELLOW, light,
            -0.82F, 1.17F, 0.38F,
            -0.75F, 1.78F, 0.49F
        );

        box(collector, pose, YELLOW, light,
             0.75F, 1.17F, 0.38F,
             0.82F, 1.78F, 0.49F
        );

        // Door panels
        box(collector, pose, DARK, light,
            -0.84F, 0.85F, -0.65F,
            -0.80F, 1.17F,  0.39F
        );

        box(collector, pose, DARK, light,
             0.80F, 0.85F, -0.65F,
             0.84F, 1.17F,  0.39F
        );

        // Door yellow markings
        box(collector, pose, YELLOW, light,
            -0.86F, 0.91F, -0.52F,
            -0.84F, 1.02F,  0.28F
        );

        box(collector, pose, YELLOW, light,
             0.84F, 0.91F, -0.52F,
             0.86F, 1.02F,  0.28F
        );

        // Door handles
        box(collector, pose, IRON, light,
            -0.88F, 1.08F, -0.32F,
            -0.84F, 1.13F, -0.15F
        );

        box(collector, pose, IRON, light,
             0.84F, 1.08F, -0.32F,
             0.88F, 1.13F, -0.15F
        );

        // =====================================
        // REAR EQUIPMENT BED
        // =====================================

        box(collector, pose, DARK, light,
            -0.83F, 0.78F, -1.66F,
             0.83F, 1.04F, -0.78F
        );

        // Rear bed walls
        box(collector, pose, BLACK, light,
            -0.85F, 1.02F, -1.66F,
            -0.73F, 1.30F, -0.78F
        );

        box(collector, pose, BLACK, light,
             0.73F, 1.02F, -1.66F,
             0.85F, 1.30F, -0.78F
        );

        box(collector, pose, BLACK, light,
            -0.85F, 1.02F, -1.66F,
             0.85F, 1.30F, -1.53F
        );

        // Yellow rear bed stripe
        box(collector, pose, YELLOW, light,
            -0.72F, 1.05F, -1.50F,
             0.72F, 1.09F, -1.46F
        );

        // Equipment boxes
        box(collector, pose, GRAY, light,
            -0.55F, 1.05F, -1.39F,
            -0.12F, 1.38F, -0.94F
        );

        box(collector, pose, IRON, light,
             0.12F, 1.05F, -1.39F,
             0.55F, 1.38F, -0.94F
        );

        // Equipment warning stripe
        box(collector, pose, YELLOW, light,
            -0.55F, 1.38F, -1.38F,
             0.55F, 1.42F, -1.33F
        );

        // =====================================
        // SIDE BODY DETAILS
        // =====================================

        // Side armor stripes
        box(collector, pose, YELLOW, light,
            -0.91F, 0.75F, -0.65F,
            -0.87F, 0.88F,  1.35F
        );

        box(collector, pose, YELLOW, light,
             0.87F, 0.75F, -0.65F,
             0.91F, 0.88F,  1.35F
        );

        // Side steps
        box(collector, pose, IRON, light,
            -1.02F, 0.40F, -0.55F,
            -0.87F, 0.53F,  0.48F
        );

        box(collector, pose, IRON, light,
             0.87F, 0.40F, -0.55F,
             1.02F, 0.53F,  0.48F
        );

        // =====================================
        // FRONT GRILLE AND LIGHTS
        // =====================================

        box(collector, pose, DARK, light,
            -0.54F, 0.79F, 1.58F,
             0.54F, 1.09F, 1.64F
        );

        // Grille bars
        for (int i = 0; i < 7; i++) {
            float x = -0.40F + i * 0.133F;

            box(collector, pose, IRON, light,
                x, 0.82F, 1.65F,
                x + 0.035F, 1.05F, 1.68F
            );
        }

        // Headlights
        box(collector, pose, LIGHT, light,
            -0.81F, 0.84F, 1.61F,
            -0.57F, 1.06F, 1.69F
        );

        box(collector, pose, LIGHT, light,
             0.57F, 0.84F, 1.61F,
             0.81F, 1.06F, 1.69F
        );

        // Orange indicators
        box(collector, pose, ORANGE, light,
            -0.94F, 0.84F, 1.58F,
            -0.82F, 1.04F, 1.68F
        );

        box(collector, pose, ORANGE, light,
             0.82F, 0.84F, 1.58F,
             0.94F, 1.04F, 1.68F
        );

        // Heavy front bumper
        box(collector, pose, IRON, light,
            -1.02F, 0.34F, 1.68F,
             1.02F, 0.59F, 1.88F
        );

        // Front winch
        box(collector, pose, DARK, light,
            -0.20F, 0.42F, 1.88F,
             0.20F, 0.57F, 1.97F
        );

        // =====================================
        // MIRRORS
        // =====================================

        box(collector, pose, IRON, light,
            -1.02F, 1.34F, 0.18F,
            -0.85F, 1.51F, 0.36F
        );

        box(collector, pose, IRON, light,
             0.85F, 1.34F, 0.18F,
             1.02F, 1.51F, 0.36F
        );

        // =====================================
        // ROOF EQUIPMENT
        // =====================================

        // Roof light bar
        box(collector, pose, BLACK, light,
            -0.69F, 1.91F, -0.08F,
             0.69F, 2.04F,  0.19F
        );

        // Left emergency light
        box(collector, pose, LIGHT, light,
            -0.59F, 2.04F, -0.05F,
            -0.34F, 2.13F, 0.15F
        );

        // Right emergency light
        box(collector, pose, ORANGE, light,
             0.34F, 2.04F, -0.05F,
             0.59F, 2.13F, 0.15F
        );

        // Radar mast
        box(collector, pose, IRON, light,
            -0.055F, 1.92F, -0.40F,
             0.055F, 2.35F, -0.29F
        );

        // Radar rotating assembly
        pose.pushPose();

        pose.translate(0.0F, 2.35F, -0.34F);
        pose.rotateDegrees(Axis.YP, radarSpin);
        pose.translate(0.0F, -2.35F, 0.34F);

        // Radar dish
        box(collector, pose, YELLOW, light,
            -0.52F, 2.32F, -0.42F,
             0.52F, 2.40F, -0.26F
        );

        box(collector, pose, BLACK, light,
            -0.13F, 2.40F, -0.45F,
             0.13F, 2.46F, -0.23F
        );

        pose.popPose();

        // Antennas
        box(collector, pose, BLACK, light,
            -0.69F, 1.91F, -0.58F,
            -0.65F, 2.43F, -0.54F
        );

        box(collector, pose, BLACK, light,
             0.65F, 1.91F, -0.58F,
             0.69F, 2.29F, -0.54F
        );

        // =====================================
        // REAR BUMPER AND LIGHTS
        // =====================================

        box(collector, pose, IRON, light,
            -0.98F, 0.36F, -1.84F,
             0.98F, 0.59F, -1.65F
        );

        box(collector, pose, ORANGE, light,
            -0.83F, 0.82F, -1.70F,
            -0.63F, 1.01F, -1.65F
        );

        box(collector, pose, ORANGE, light,
             0.63F, 0.82F, -1.70F,
             0.83F, 1.01F, -1.65F
        );

        // =====================================
        // OFF-ROAD WHEELS
        // =====================================

        wheel(collector, pose, light, -1, 1, wheelSpin);
        wheel(collector, pose, light,  1, 1, wheelSpin);
        wheel(collector, pose, light, -1, -1, wheelSpin);
        wheel(collector, pose, light,  1, -1, wheelSpin);
    }

    private static void wheel(
        SubmitNodeCollector collector,
        PoseStack pose,
        int light,
        int side,
        int front,
        float spin
    ) {
        float cx = side * 0.94F;
        float cy = 0.48F;
        float cz = front * 1.08F;

        pose.pushPose();

        // Rotate wheel tread around axle
        pose.translate(cx, cy, cz);
        pose.rotateDegrees(Axis.XP, spin);
        pose.translate(-cx, -cy, -cz);

        // Large rubber tire
        cylinderX(
            collector, pose, BLACK, light,
            cx, cy, cz, 0.42F, 0.43F
        );

        // Wheel hub
        cylinderX(
            collector, pose, IRON, light,
            cx, cy, cz, 0.44F, 0.24F
        );

        // Yellow hub center
        cylinderX(
            collector, pose, YELLOW, light,
            cx + side * 0.225F, cy, cz, 0.035F, 0.11F
        );

        // Tire tread blocks
        for (int i = 0; i < 12; i++) {
            pose.pushPose();

            pose.translate(cx, cy, cz);
            pose.rotateDegrees(Axis.XP, i * 30.0F);
            pose.translate(-cx, -cy, -cz);

            box(
                collector, pose, DARK, light,
                cx - 0.18F, cy + 0.38F, cz - 0.075F,
                cx + 0.18F, cy + 0.46F, cz + 0.075F
            );

            pose.popPose();
        }

        pose.popPose();
    }

    private static void cylinderX(
        SubmitNodeCollector collector,
        PoseStack pose,
        Identifier texture,
        int light,
        float cx,
        float cy,
        float cz,
        float width,
        float radius
    ) {
        collector.submitCustomGeometry(
            pose,
            RenderTypes.entitySolid(texture),
            (matrix, buffer) -> {

                int segments = 20;

                float x0 = cx - width / 2.0F;
                float x1 = cx + width / 2.0F;

                for (int i = 0; i < segments; i++) {

                    float a0 = (float) (
                        i * Math.PI * 2.0 / segments
                    );

                    float a1 = (float) (
                        (i + 1) * Math.PI * 2.0 / segments
                    );

                    float mid = (a0 + a1) * 0.5F;

                    float y0 = cy + (float) Math.cos(a0) * radius;
                    float z0 = cz + (float) Math.sin(a0) * radius;

                    float y1 = cy + (float) Math.cos(a1) * radius;
                    float z1 = cz + (float) Math.sin(a1) * radius;

                    float ny = (float) Math.cos(mid);
                    float nz = (float) Math.sin(mid);

                    quad(
                        buffer, matrix, light,
                        x0, y0, z0,
                        x0, y1, z1,
                        x1, y1, z1,
                        x1, y0, z0,
                        0, ny, nz
                    );

                    triangle(
                        buffer, matrix, light,
                        x0, cy, cz,
                        x0, y1, z1,
                        x0, y0, z0,
                        -1, 0, 0
                    );

                    triangle(
                        buffer, matrix, light,
                        x1, cy, cz,
                        x1, y0, z0,
                        x1, y1, z1,
                        1, 0, 0
                    );
                }
            }
        );
    }

    private static void box(
        SubmitNodeCollector collector,
        PoseStack pose,
        Identifier texture,
        int light,
        float x0, float y0, float z0,
        float x1, float y1, float z1
    ) {
        collector.submitCustomGeometry(
            pose,
            RenderTypes.entitySolid(texture),
            (matrix, buffer) -> {

                // Front
                quad(buffer, matrix, light,
                    x0,y0,z1, x1,y0,z1,
                    x1,y1,z1, x0,y1,z1,
                    0,0,1
                );

                // Back
                quad(buffer, matrix, light,
                    x1,y0,z0, x0,y0,z0,
                    x0,y1,z0, x1,y1,z0,
                    0,0,-1
                );

                // Right
                quad(buffer, matrix, light,
                    x1,y0,z1, x1,y0,z0,
                    x1,y1,z0, x1,y1,z1,
                    1,0,0
                );

                // Left
                quad(buffer, matrix, light,
                    x0,y0,z0, x0,y0,z1,
                    x0,y1,z1, x0,y1,z0,
                    -1,0,0
                );

                // Top
                quad(buffer, matrix, light,
                    x0,y1,z1, x1,y1,z1,
                    x1,y1,z0, x0,y1,z0,
                    0,1,0
                );

                // Bottom
                quad(buffer, matrix, light,
                    x0,y0,z0, x1,y0,z0,
                    x1,y0,z1, x0,y0,z1,
                    0,-1,0
                );
            }
        );
    }

    private static void quad(
        VertexConsumer buffer,
        PoseStack.Pose pose,
        int light,
        float x0, float y0, float z0,
        float x1, float y1, float z1,
        float x2, float y2, float z2,
        float x3, float y3, float z3,
        float nx, float ny, float nz
    ) {
        vertex(buffer, pose, light, x0,y0,z0, 0,0, nx,ny,nz);
        vertex(buffer, pose, light, x1,y1,z1, 1,0, nx,ny,nz);
        vertex(buffer, pose, light, x2,y2,z2, 1,1, nx,ny,nz);
        vertex(buffer, pose, light, x3,y3,z3, 0,1, nx,ny,nz);
    }

    private static void triangle(
        VertexConsumer buffer,
        PoseStack.Pose pose,
        int light,
        float x0, float y0, float z0,
        float x1, float y1, float z1,
        float x2, float y2, float z2,
        float nx, float ny, float nz
    ) {
        vertex(buffer, pose, light, x0,y0,z0, 0.5F,0.5F, nx,ny,nz);
        vertex(buffer, pose, light, x1,y1,z1, 0,0, nx,ny,nz);
        vertex(buffer, pose, light, x2,y2,z2, 1,0, nx,ny,nz);
    }

    private static void vertex(
        VertexConsumer buffer,
        PoseStack.Pose pose,
        int light,
        float x, float y, float z,
        float u, float v,
        float nx, float ny, float nz
    ) {
        buffer.addVertex(pose, x, y, z)
            .setColor(-1)
            .setUv(u, v)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(light)
            .setNormal(pose, nx, ny, nz);
    }
}
