# ExtensionLib

*A library for managing optional client mods for Minecraft*

## Usage 

### Clients
When this mod is installed, a new menu option will be added to both the edit server and edit world screens. This option
allows users to configure which extensions are enabled on a per-server or per-world basis. Simply toggle them as you
would experimental features. Servers will not be informed of disabled extensions.

### Servers
Extension requirements can be configured in the `config/extensionlib/config.json` file. The `required_extension` field
is a mapping of extension IDs to version predicates. <br/>
For example: a configuration which requires any version of the `test:test` extension:
```json
{
  "required_extensions": {
    "test:test": "*"
  }
}
```
Or, a configuration which requires at least a specific version of the `test:test` extension:
```json
{
  "required_extensions": {
    "test:test": ">=1.2.0"
  }
}
```

### Developers
Developers can use this library to manage optional client mods. Extensions should always assumed to be optional, so 
joining vanilla servers doesn't cause issues. <br/>
For example, to register an extension:
```java
ResourceLocation id = ResourceLocation.tryParse("test:test");
ExtensionRegistry.registerExtension(id, ExtensionRegistry.makeVersion(1,0,0));
```

To query if a player has a particular extension enabled on the server:
```java
if(PlayerExtensions.hasExtension(player, id)) {     
        ...
}
```
Or, to query if the client has a particular extension enabled:
```java 
if(ClientExtensions.hasExtension(id)) {
        ...
}
```