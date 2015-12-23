package com.mbrlabs.mundus.core.kryo.descriptors;

import com.badlogic.gdx.math.Vector3;
import com.mbrlabs.mundus.core.Scene;
import com.mbrlabs.mundus.core.project.ProjectContext;
import com.mbrlabs.mundus.model.MModel;
import com.mbrlabs.mundus.terrain.Terrain;
import com.mbrlabs.mundus.terrain.TerrainInstance;
import com.mbrlabs.mundus.utils.Log;

import java.util.List;

/**
 * @author Marcus Brummer
 * @version 17-12-2015
 */
public class DescriptorConverter {

    public static ModelDescriptor convert(MModel model) {
        ModelDescriptor descriptor = new ModelDescriptor();
        descriptor.setName(model.name);
        descriptor.setId(model.id);
        descriptor.setG3dbPath(model.g3dbPath);
        return descriptor;
    }

    public static MModel convert(ModelDescriptor modelDescriptor) {
        MModel model = new MModel();
        model.id = modelDescriptor.getId();
        model.name = modelDescriptor.getName();
        model.g3dbPath = modelDescriptor.getG3dbPath();

        return model;
    }

    public static TerrainDescriptor convert(Terrain terrain) {
        TerrainDescriptor descriptor = new TerrainDescriptor();
        descriptor.setId(terrain.id);
        descriptor.setName(terrain.name);
        descriptor.setPath(terrain.terraPath);
        descriptor.setWidth(terrain.terrainWidth);
        descriptor.setDepth(terrain.terrainDepth);
        descriptor.setVertexResolution(terrain.vertexResolution);
        return descriptor;
    }

    public static Terrain convert(TerrainDescriptor terrainDescriptor) {
        Terrain terrain = new Terrain(terrainDescriptor.getVertexResolution());
        terrain.terrainWidth = terrainDescriptor.getWidth();
        terrain.terrainDepth = terrainDescriptor.getDepth();
        terrain.terraPath = terrainDescriptor.getPath();
        terrain.id = terrainDescriptor.getId();
        terrain.name = terrainDescriptor.getName();

        return terrain;
    }

    public static TerrainInstanceDescriptor convert(TerrainInstance terrain) {
        TerrainInstanceDescriptor descriptor = new TerrainInstanceDescriptor();
        descriptor.setId(terrain.id);
        descriptor.setName(terrain.name);
        descriptor.setTerrainID(terrain.terrain.id);
        Vector3 pos = terrain.getPosition();
        descriptor.setPosX(pos.x);
        descriptor.setPosZ(pos.z);

        return descriptor;
    }

    public static TerrainInstance convert(TerrainInstanceDescriptor terrainDescriptor, List<Terrain> terrains) {
        // find terrain
        Terrain terrain = null;
        for(Terrain t : terrains) {
            if(terrainDescriptor.getTerrainID() == t.id) {
                terrain = t;
                break;
            }
        }

        if(terrain == null) {
            Log.fatal("Terrain for TerrainInstance not found");
            return null;
        }

        final TerrainInstance terrainInstance = new TerrainInstance(terrain);
        terrainInstance.transform.setTranslation(terrainDescriptor.getPosX(), 0, terrainDescriptor.getPosZ());
        terrainInstance.name = terrainDescriptor.getName();
        terrainInstance.id = terrainDescriptor.getId();

        return terrainInstance;
    }


    public static SceneDescriptor convert(Scene scene) {
        // TODO enviroenment, entities
        SceneDescriptor descriptor = new SceneDescriptor();
        descriptor.setName(scene.getName());
        descriptor.setId(scene.getId());

        // entities
//        for(MModelInstance entity : scene.entities) {
//            descriptor.getEntities().add(convert(entity));
//        }

        // terrains
        for(TerrainInstance terrain : scene.terrains) {
            descriptor.getTerrains().add(convert(terrain));
        }

        return descriptor;
    }

    public static Scene convert(SceneDescriptor sceneDescriptor, List<Terrain> terrains, List<MModel> models) {
        // TODO enviroenment, entities
        Scene scene = new Scene();
        scene.setId(sceneDescriptor.getId());
        scene.setName(sceneDescriptor.getName());

        // terrains
        for(TerrainInstanceDescriptor terrainDescriptor : sceneDescriptor.getTerrains()) {
            scene.terrains.add(convert(terrainDescriptor, terrains));
        }

        return scene;
    }


    public static ProjectDescriptor convert(ProjectContext project) {
        ProjectDescriptor descriptor = new ProjectDescriptor();
        descriptor.setName(project.name);
        descriptor.setId(project.id);
        descriptor.setNextAvailableID(project.getCurrentUUID());
        // terrains
        for(Terrain terrain : project.terrains) {
            descriptor.getTerrains().add(convert(terrain));
        }
        // models
        for(MModel model : project.models) {
            descriptor.getModels().add(convert(model));
        }
        // scenes
        for(Scene scene : project.scenes) {
            descriptor.getScenes().add(convert(scene));
        }

        return descriptor;
    }

    public static ProjectContext convert(ProjectDescriptor projectDescriptor) {
        ProjectContext context = new ProjectContext(projectDescriptor.getNextAvailableID());
        context.name = projectDescriptor.getName();
        // models
        for(ModelDescriptor model : projectDescriptor.getModels()) {
            context.models.add(convert(model));
        }
        // terrains
        for(TerrainDescriptor terrain : projectDescriptor.getTerrains()) {
            context.terrains.add(convert(terrain));
        }
        // scenes
        for(SceneDescriptor scene : projectDescriptor.getScenes()) {
            context.scenes.add(convert(scene, context.terrains, context.models));
        }

        context.loaded = false;
        return context;
    }




}
