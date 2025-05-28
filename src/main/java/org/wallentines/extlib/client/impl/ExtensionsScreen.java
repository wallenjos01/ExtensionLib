package org.wallentines.extlib.client.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.semver4j.Semver;
import org.wallentines.extlib.api.ExtensionRegistry;

import java.util.*;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class ExtensionsScreen extends Screen {

    private static final Component TITLE = Component.translatable("screen.extensions.title");
    public static final Component BUTTON = Component.translatable("button.extensions");

    private final Screen lastScreen;
    private final ExtensionListHolder serverData;
    private final List<Entry> enabledExtensions;

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private ScrollArea scrollArea;

    public ExtensionsScreen(Screen lastScreen, ExtensionListHolder serverData) {
        super(TITLE);

        this.lastScreen = lastScreen;
        this.serverData = serverData;
        this.enabledExtensions = new ArrayList<>();

        for (Map.Entry<ResourceLocation, Semver> entry : ExtensionRegistry.getAllExtensions().entrySet()) {
            ResourceLocation loc = entry.getKey();

            String nameKey = loc.getNamespace() + ".extension." + loc.getPath() + ".name";
            Component name = Component.translatable(nameKey).append(" (" + entry.getValue().toString() + ")");
            Component description = Component
                    .translatable(loc.getNamespace() + ".extension." + loc.getPath() + ".description")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));

            enabledExtensions.add(new Entry(loc, Language.getInstance().getOrDefault(nameKey), name, description,
                    serverData.getEnabledExtensions().contains(loc)));
            enabledExtensions.sort(Comparator.comparing(e -> e.sortName, String::compareToIgnoreCase));
        }

    }

    protected void init() {

        this.layout.addTitleHeader(TITLE, font);

        LinearLayout contents = layout.addToContents(LinearLayout.vertical());

        if (enabledExtensions.isEmpty()) {

            contents.addChild(new StringWidget(Component.translatable("screen.extensions.empty"), font));

        } else {
            GridLayout gridLayout = new GridLayout().rowSpacing(4);

            int row = 0;
            for (Entry entry : enabledExtensions) {

                LinearLayout nameAndDesc = new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL).spacing(4);
                nameAndDesc.addChild(new StringWidget(0, 0, 299 - 44, 9, entry.name, font).alignLeft());
                nameAndDesc
                        .addChild(new MultiLineTextWidget(entry.description, font).setMaxWidth(299 - 44).setMaxRows(2));

                gridLayout.addChild(nameAndDesc, row, 0);
                gridLayout.addChild(CycleButton
                        .onOffBuilder(entry.enabled)
                        .displayOnlyValue()
                        .create(0, 0, 44, Button.DEFAULT_HEIGHT,
                                Component.empty(),
                                (cycleButton, object) -> entry.enabled = object),
                        row, 2);
                row++;
            }

            this.scrollArea = new ScrollArea(gridLayout, 310, 130);
            contents.addChild(scrollArea);
        }

        layout.addToFooter(Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    @Override
    public void onClose() {
        serverData.setEnabledExtensions(
                enabledExtensions.stream().filter(ent -> ent.enabled).map(ent -> ent.id).toList());
        Minecraft.getInstance().setScreen(this.lastScreen);
    }

    @Override
    protected void repositionElements() {

        if (scrollArea != null) {
            this.scrollArea.setHeight(130);
        }

        this.layout.arrangeElements();
        if (scrollArea != null) {
            int i = this.height - this.layout.getFooterHeight() - this.scrollArea.getRectangle().bottom();
            this.scrollArea.setHeight(this.scrollArea.getHeight() + i);
            this.scrollArea.refreshScrollAmount();
        }
    }

    private static class Entry {
        final ResourceLocation id;
        final String sortName;
        final Component name;
        final Component description;

        boolean enabled;

        private Entry(ResourceLocation id, String sortName, Component name, Component description, boolean enabled) {
            this.id = id;
            this.sortName = sortName;
            this.name = name;
            this.description = description;
            this.enabled = enabled;
        }
    }

    private static class ScrollArea extends AbstractContainerWidget {
        private final List<AbstractWidget> children = new ArrayList<>();
        private final Layout layout;

        public ScrollArea(final Layout layout, final int i, final int j) {
            super(0, 0, i, j, CommonComponents.EMPTY);
            this.layout = layout;
            layout.visitWidgets(this::addWidget);
        }

        public void addWidget(AbstractWidget abstractWidget) {
            this.children.add(abstractWidget);
        }

        protected int contentHeight() {
            return this.layout.getHeight();
        }

        protected double scrollRate() {
            return 10.0F;
        }

        protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            guiGraphics.enableScissor(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height);
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(0.0F, (float) -this.scrollAmount());

            for (AbstractWidget abstractWidget : this.children) {
                abstractWidget.render(guiGraphics, i, j, f);
            }

            guiGraphics.pose().popMatrix();
            guiGraphics.disableScissor();
            this.renderScrollbar(guiGraphics);
        }

        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }

        public ScreenRectangle getBorderForArrowNavigation(ScreenDirection screenDirection) {
            return new ScreenRectangle(this.getX(), this.getY(), this.width, this.contentHeight());
        }

        public void setFocused(@Nullable GuiEventListener guiEventListener) {
            super.setFocused(guiEventListener);
            if (guiEventListener != null) {
                ScreenRectangle screenRectangle = this.getRectangle();
                ScreenRectangle screenRectangle2 = guiEventListener.getRectangle();
                int i = (int) ((double) screenRectangle2.top() - this.scrollAmount() - (double) screenRectangle.top());
                int j = (int) ((double) screenRectangle2.bottom() - this.scrollAmount()
                        - (double) screenRectangle.bottom());
                if (i < 0) {
                    this.setScrollAmount(this.scrollAmount() + (double) i - (double) 14.0F);
                } else if (j > 0) {
                    this.setScrollAmount(this.scrollAmount() + (double) j + (double) 14.0F);
                }

            }
        }

        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        public void setX(int i) {
            super.setX(i);
            this.layout.setX(i);
            this.layout.arrangeElements();
        }

        public void setY(int i) {
            super.setY(i);
            this.layout.setY(i);
            this.layout.arrangeElements();
        }

        public Collection<? extends NarratableEntry> getNarratables() {
            return this.children;
        }
    }

}
