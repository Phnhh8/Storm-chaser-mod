
package dev.stormchasers.client.vehicle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class ChaserTruckModel {

    private static final Identifier BLACK =
        id("block/black_concrete");

    private static final Identifier GRAY =
        id("block/gray_concrete");

    private static final Identifier YELLOW =
        id("block/yellow_concrete");

    private static final Identifier IRON =
        id("block/iron_block");

    private static final Identifier GLASS =
        id("block/light_blue_stained_glass");

    private static final Identifier LIGHT =
        id("block/sea_lantern");

    private static final Identifier ORANGE =
        id("block/orange_concrete");

    private static final Identifier DARK =
        id("block/polished_deepslate");

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

        // Main armored chassis
        box(collector, pose, BLACK, light,
            -0.92F, 0.38F, -1.62F,
             0.92F, 0.82F,  1.60F
        );

        box(collector, pose, DARK, light,
            -0.82F, 0.43F, -1.50F,
             0.82F, 0.72F,  1.48F
        );

        // Front hood
        box(collector, pose, GRAY, light,
            -0.86F, 0.74F, 0.36F,
             0.86F, 1.18F, 1.58F
        );

        box(collector, pose, BLACK, light,
            -0.70F, 1.18F, 0.55F,
             0.70F, 1.24F, 1.43F
        );

        // Hood warning stripes
        box(collector, pose, YELLOW, light,
            -0.76F, 1.245F, 0.72F,
            -0.63F, 1.255F, 1.35F
        );

        box(collector, pose, YELLOW, light,
             0.63F, 1.245F, 0.72F,
             0.76F, 1.255F, 1.35F
        );

        // Cabin
        box(collector, pose, BLACK, light,
            -0.82F, 0.73F, -0.78F,
             0.82F, 1.76F,  0.48F
        );

        // Front windshield
        box(collector, pose, GLASS, light,
            -0.66F, 1.20F, 0.49F,
             0.66F, 1.66F, 0.55F
        );

        // Rear window
        box(collector, pose, GLASS, light,
            -0.59F, 1.20F, -0.82F,
             0.59F, 1.59F, -0.77F
        );

        // Side windows
        box(collector, pose, GLASS, light,
            -0.86F, 1.20F, -0.58F,
            -0.82F, 1.61F,  0.32F
        );

        box(collector, pose, GLASS, light,
             0.82F, 1.20F, -0.58F,
             0.86F, 1.61F,  0.32F
        );

        // Roof armor rails - open center for driver
        box(collector, pose, BLACK, light,
            -0.90F, 1.77F, -0.78F,
            -0.76F, 1.93F,  0.49F
        );

        box(collector, pose, BLACK, light,
             0.76F, 1.77F, -0.78F,
             0.90F, 1.93F,  0.49F
        );

        box(collector, pose, BLACK, light,
            -0.82F, 1.77F, 0.37F,
             0.82F, 1.93F, 0.49F
        );

        box(collector, pose, BLACK, light,
            -0.82F, 1.77F, -0.78F,
             0.82F, 1.93F, -0.66F
        );

        // Rear equipment bed
        box(collector, pose, DARK, light,
            -0.82F, 0.73F, -1.56F,
             0.82F, 1.02F, -0.78F
        );

        box(collector, pose, BLACK, light,
            -0.88F, 0.98F, -1.53F,
            -0.72F, 1.22F, -0.78F
        );

        box(collector, pose, BLACK, light,
             0.72F, 0.98F, -1.53F,
             0.88F, 1.22F, -0.78F
        );

        // Yellow side warning stripes
        box(collector, pose, YELLOW, light,
            -0.96F, 0.80F, -0.65F,
            -0.91F, 0.94F,  1.42F
        );

        box(collector, pose, YELLOW, light,
             0.91F, 0.80F, -0.65F,
             0.96F, 0.94F,  1.42F
        );

        // Doors and handles
        box(collector, pose, GRAY, light,
            -0.88F, 1.00F, -0.48F,
            -0.86F, 1.08F, -0.25F
        );

        box(collector, pose, GRAY, light,
             0.86F, 1.00F, -0.48F,
             0.88F, 1.08F, -0.25F
        );

        // Side steps
        box(collector, pose, IRON, light,
            -1.02F, 0.40F, -0.68F,
            -0.88F, 0.54F,  0.55F
        );

        box(collector, pose, IRON, light,
             0.88F, 0.40F, -0.68F,
             1.02F, 0.54F,  0.55F
        );

        // Front grille
        box(collector, pose, BLACK, light,
            -0.55F, 0.79F, 1.59F,
             0.55F, 1.08F, 1.65F
        );

        for (int i = 0; i < 5; i++) {
            float x = -0.38F + i * 0.19F;

            box(collector, pose, IRON, light,
                x, 0.81F, 1.66F,
                x + 0.035F, 1.05F, 1.68F
            );
        }

        // Front bumper
        box(collector, pose, IRON, light,
            -1.02F, 0.36F, 1.66F,
             1.02F, 0.61F, 1.84F
        );

        // Winch
        box(collector, pose, DARK, light,
            -0.20F, 0.43F, 1.84F,
             0.20F, 0.58F, 1.94F
        );

        // Headlights
        box(collector, pose, LIGHT, light,
            -0.80F, 0.85F, 1.66F,
            -0.56F, 1.04F, 1.70F
        );

        box(collector, pose, LIGHT, light,
             0.56F, 0.85F, 1.66F,
             0.80F, 1.04F, 1.70F
        );

        // Orange indicators
        box(collector, pose, ORANGE, light,
            -0.94F, 0.85F, 1.64F,
            -0.82F, 1.03F, 1.69F
        );

        box(collector, pose, ORANGE, light,
             0.82F, 0.85F, 1.64F,
             0.94F, 1.03F, 1.69F
        );

        // Mirrors
        box(collector, pose, IRON, light,
            -1.05F, 1.33F, 0.15F,
            -0.86F, 1.52F, 0.38F
        );

        box(collector, pose, IRON, light,
             0.86F, 1.33F, 0.15F,
             1.05F, 1.52F, 0.38F
        );

        // Roof light bar
        box(collector, pose, BLACK, light,
            -0.67F, 1.94F, -0.15F,
             0.67F, 2.06F,  0.12F
        );

        box(collector, pose, LIGHT, light,
            -0.58F, 2.06F, -0.12F,
            -0.34F, 2.13F, 0.08F
        );

        box(collector, pose, LIGHT, light,
             0.34F, 2.06F, -0.12F,
             0.58F, 2.13F, 0.08F
        );

        // Radar mast
        box(collector, pose, IRON, light,
            -0.045F, 1.92F, -0.28F,
             0.045F, 2.33F, -0.19F
        );

        // Rotating radar dish
        pose.pushPose();
        pose.translate(0.0F, 2.33F, -0.23F);
        pose.rotateDegrees(Axis.YP, radarSpin);
        pose.translate(0.0F, -2.33F, 0.23F);

        box(collector, pose, YELLOW, light,
            -0.48F, 2.31F, -0.28F,
             0.48F, 2.39F, -0.18F
        );

        box(collector, pose, BLACK, light,
            -0.12F, 2.40F, -0.31F,
             0.12F, 2.44F, -0.15F
        );

        pose.popPose();

        // Antennas
        box(collector, pose, BLACK, light,
            -0.70F, 1.92F, -0.52F,
            -0.67F, 2.48F, -0.49F
        );

        box(collector, pose, BLACK, light,
             0.68F, 1.92F, -0.52F,
             0.71F, 2.30F, -0.49F
        );

        // Rear bumper
        box(collector, pose, IRON, light,
            -0.98F, 0.38F, -1.78F,
             0.98F, 0.60F, -1.60F
        );

        // Rear lights
        box(collector, pose, ORANGE, light,
            -0.83F, 0.82F, -1.62F,
            -0.63F, 1.00F, -1.58F
        );

        box(collector, pose, ORANGE, light,
             0.63F, 0.82F, -1.62F,
             0.83F, 1.00F, -1.58F
        );

        // Four rotating off-road wheels
        wheel(collector, pose, light, -1, -1, wheelSpin);
        wheel(collector, pose, light,  1, -1, wheelSpin);
        wheel(collector, pose, light, -1,  1, wheelSpin);
        wheel(collector, pose, light,  1,  1, wheelSpin);
    }

    private static void wheel(
        SubmitNodeCollector collector,
        PoseStack pose,
        int light,
        int side,
        int front,
        float spin
    ) {
        float cx = side * 0.98F;
        float cy = 0.45F;
        float cz = front * 1.05F;

        pose.pushPose();

        pose.translate(cx, cy, cz);
        pose.rotateDegrees(Axis.XP, spin);
        pose.translate(-cx, -cy, -cz);

        // Rubber tire
        cylinderX(
            collector, pose, BLACK, light,
            cx, cy, cz, 0.34F, 0.43F
        );

        // Metal wheel hub
        cylinderX(
            collector, pose, IRON, light,
            cx, cy, cz, 0.38F, 0.23F
        );

        // Six visible spokes
        float outerX = cx + side * 0.20F;

        for (int i = 0; i < 6; i++) {
            pose.pushPose();

            pose.translate(cx, cy, cz);
            pose.rotateDegrees(Axis.XP, i * 60.0F);
            pose.translate(-cx, -cy, -cz);

            box(
                collector, pose, GRAY, light,
                outerX - 0.025F,
                cy - 0.025F,
                cz - 0.16F,
                outerX + 0.025F,
                cy + 0.025F,
                cz + 0.16F
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

                int segments = 12;

                float x0 = cx - width / 2.0F;
                float x1 = cx + width / 2.0F;

                for (int i = 0; i < segments; i++) {

                    float a0 =
                        (float) (i * Math.PI * 2.0 / segments);

                    float a1 =
                        (float) ((i + 1) * Math.PI * 2.0 / segments);

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

                    // Left wheel cap
                    triangle(
                        buffer, matrix, light,
                        x0, cy, cz,
                        x0, y1, z1,
                        x0, y0, z0,
                        -1, 0, 0
                    );

                    // Right wheel cap
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
