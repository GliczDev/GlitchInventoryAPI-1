# GlitchInventoryAPI
**This API requires ProtocolLib!** [![CodeFactor](https://www.codefactor.io/repository/github/gliczdev/glitchinventoryapi/badge)](#)

## JitPack
> If you want to always use the latest commit build, please use `main-SNAPSHOT` instead of version tag
<details><summary>Maven</summary>
<p>

Repository
```xaml
<repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
</repository>
```
Dependency
```xaml
<dependency>
	<groupId>com.github.GliczDev</groupId>
	<artifactId>GlitchInventoryAPI</artifactId>
	<version>TAG</version>
</dependency>
```
</p>
</details>
<details><summary>Gradle</summary>
<p>

Repository
```gradle
repositories {
	maven { url 'https://jitpack.io' }
}
```
Dependency
```gradle
dependencies {
	implementation 'com.github.GliczDev:GlitchInventoryAPI:TAG'
}
```
</p>
</details>


## Shading
If you want to shade this API into your plugin, you have to initialize it in `onEnable()` and uninitialize it in `onDisable()`, for example:
```java
private static GlitchInventoryAPI glitchInventoryAPI;

@Override
public void onEnable() {
        glitchInventoryAPI = new GlitchInventoryAPI(this);
        glitchInventoryAPI.initialize();
}

@Override
public void onDisable() {
        glitchInventoryAPI.uninitialize();
}
```

## Usage
### Basic example
```java
GlitchInventory.simple() //Create simple inventory builder
	.inventoryType(InventoryType.Chest_9x6) //Set inventory type to chest 9x6
	.title(Title.simple("Title")) //Set title to SimpleTitle object
	.create() //Create simple inventory from builder
	.setSlot(10, ItemBuilder.from(Material.STONE) 
		.asGuiItem(e -> e.getPlayer().sendMessage("Simple GUI created with GlitchInventoryAPI!"))) //Set slot to stone item with click action
        .open(player); //Open inventory to player
```
### Inventory types
There are two different inventory types:
- Simple (`GlitchInventory.simple()`) - normal inventory without any special abilities
- Paged (`GlitchInventory.paged()`) - allows you to create inventory pages

### Title types
There are two different title types:
- Simple (`Title.simple()`) - normal title without any special abilities
- Animated (`Title.animated()`) - allows you to create animated title text
- Random (`Title.random()`) - allows you to create random title text
