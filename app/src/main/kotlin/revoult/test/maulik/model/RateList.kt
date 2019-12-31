package revoult.test.maulik.model

/**
 * Rate list model for parsing data from retrofit
 */
data class RateList(val base: String, val date: String, val rates: Map<String, Float>)
{
}