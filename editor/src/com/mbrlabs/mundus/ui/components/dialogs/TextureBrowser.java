package com.mbrlabs.mundus.ui.components.dialogs;

import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.mbrlabs.mundus.core.Inject;
import com.mbrlabs.mundus.core.Mundus;
import com.mbrlabs.mundus.core.project.ProjectContext;
import com.mbrlabs.mundus.events.ProjectChangedEvent;
import com.mbrlabs.mundus.events.TextureImportEvent;
import com.mbrlabs.mundus.ui.widgets.TextureGrid;

/**
 * @author Marcus Brummer
 * @version 28-01-2016
 */
public class TextureBrowser extends BaseDialog implements TextureImportEvent.TextureImportListener, ProjectChangedEvent.ProjectChangedListener {

    private TextureGrid textureGrid;
    private VisScrollPane scrollPane;

    @Inject
    private ProjectContext projectContext;

    public TextureBrowser() {
        super("Select a texture");
        Mundus.inject(this);
        Mundus.registerEventListener(this);

        textureGrid = new TextureGrid(60, 7, projectContext.textures);
        scrollPane = new VisScrollPane(textureGrid);
        scrollPane.setScrollingDisabled(true, false);
        add(scrollPane).maxSize(600, 400).size(600, 400);
    }

    public void setTextureListener(TextureGrid.OnTextureClickedListener listener) {
        textureGrid.setListener(listener);
    }

    @Override
    public void onProjectChanged(ProjectChangedEvent projectChangedEvent) {
        textureGrid.setTextures(projectContext.textures);
    }

    @Override
    public void onTextureImported(TextureImportEvent importEvent) {
        textureGrid.setTextures(projectContext.textures);
    }
}