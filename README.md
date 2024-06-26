# ⚠️ OUTDATED. See: https://github.com/GliczDev/GlitchInventoryAPI

# GlitchInventoryAPI [![CodeFactor](https://www.codefactor.io/repository/github/gliczdev/glitchinventoryapi/badge)](#)
<div align="center">
<a href="https://discord.gg/ZRuaXh3P63"><img alt="discord-plural" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/discord-plural_46h.png"></a>
<a href="#"><img alt="paper" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/paper_46h.png"></a>
<a href="https://jitpack.io/#GliczDev/GlitchInventoryAPI"><img alt="jitpack" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/jitpack_46h.png"></a>
</div>

## Supported versions
This API supports all versions >= 1.17.1!

## JitPack
> If you want to always use the latest commit build, please use `*branch*-SNAPSHOT` instead of version tag
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
<details><summary>Gradle (Kotlin DSL)</summary>
<p>

Repository
```gradle
repositories {
	maven("https://jitpack.io")
}
```
Dependency
```gradle
dependencies {
	implementation("com.github.GliczDev:GlitchInventoryAPI:TAG")
}
```
</p>
</details>
<details><summary>Gradle (Groovy DSL)</summary>
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
If you want to shade this API into your plugin, you have to load it in `onEnable()`
```java
@Override
public void onEnable() {
        GlitchInventoryAPI.load(this);
}
```

## Usage
### Basic example
```java
GlitchInventory.simple(3) //Create simple inventory with 3 rows (Bukkit InventoryType can be also used)
	.setTitle(Title.simple("GlitchInventoryAPI") //Set title to GlitchInventoryAPI
	.setItem(10, ItemBuilder.of(Material.STONE) 
		.asGuiItem(e -> {
			e.getPlayer().sendMessage("Simple GUI created with GlitchInventoryAPI!")
		})) //Set slot to stone item with click action
        .open(player); //Open inventory to player
```
### Inventory types
- Simple (`GlitchInventory.simple()`) - a normal inventory
- Paginated (`GlitchInventory.paginated()`) - a paginated inventory
- Merchant (`GlitchInventory.merchant()`) - a merchant inventory

### Title types
- Simple (`Title.simple()`) - a normal title
- Animated (`Title.animated()`) - an animated title <ins>**//Soon**</ins>
- Random (`Title.random()`) - a randomly animated title <ins>**//Soon**</ins>
