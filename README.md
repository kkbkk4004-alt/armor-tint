# Armor Tint (Fabric, Minecraft 1.21.4)

Client-side mod. Armor worn by **any player you can see** (you and others) changes color as it
gets close to breaking: no tint above half durability, then yellow, orange and red. Below 10%
left it blinks.

- Press **G** (rebindable in Controls) to switch it on/off.
- `.minecraft/config/armortint.json`: `startFraction` (when tinting starts, default 0.5) and
  `flash` (blink when almost broken).
- Only you see it, and it works on any server because nothing is sent over the network.
  You see other players' armor condition because the server tells every client what they wear.

## Notes

- The tint multiplies the armor's own colors, so light armor (gold, iron, leather) shows it
  strongest and dark armor (netherite) shows it more subtly.
- Armor trims and the enchant glint on that piece are tinted too.
- Elytra and shields are not armor and are not changed.

## Build

Push to GitHub; the Actions workflow builds the jar (artifact `armor-tint-jar`).
Needs Fabric API for 1.21.4. Written for the Yarn mappings of 1.21.4; if the build or game
reports that `renderArmor` can't be found, the render code differs and needs a small fix.
