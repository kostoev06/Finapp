package com.finapp.feature.common.theme

import com.finapp.core.settings.api.model.BrandColorOption


fun brandPalette(option: BrandColorOption): BrandPalette = when (option) {
    BrandColorOption.GREEN -> BrandPalette(GreenPrimary, GreenPrimaryLight, onGreen, onGreenContainer)
    BrandColorOption.BLUE -> BrandPalette(BluePrimary, BluePrimaryLight, onBlue, onGreenContainer)
    BrandColorOption.ORANGE -> BrandPalette(OrangePrimary, OrangePrimaryLight, onOrange, onOrangeContainer)
    BrandColorOption.PURPLE -> BrandPalette(PurplePrimary, PurplePrimaryLight, onPurple, onPurpleContainer)
    BrandColorOption.RED -> BrandPalette(RedPrimary, RedPrimaryLight, onRed, onRedContainer)
}