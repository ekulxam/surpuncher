# Surpuncher
Surprise! You've been punched

Surpuncher adds a new item, the Extending Fist. On use, a fist will _extend_ out from the item, dealing damage and knocking back* entities in its path. After some time, the fist will return to the user. Fists can be dyed in a crafting table.

### Flurry
The Flurry enchantment increases the number of fists fired. The total number of fists is equal to `level + 1`. In an attempt to balance this enchantment, randomness is also increased per level (`0.01 * level`).

Happy punching!

This mod was made for Modfest: Toybox.</br>![Modfest: Toybox badge](https://raw.githubusercontent.com/ModFest/art/v2/badge/svg/toybox/cozy.svg)

*The entity's velocity becomes `original velocity + (fist velocity * 1.5)`