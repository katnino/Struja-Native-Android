package com.noniboy.struja.domain.tariff

import com.noniboy.struja.data.model.BlockBreakdown

object TariffRates {
    const val SERVICE_FEE = 2.48
    const val POWER_FLAT_RATE = 0.2467
    const val POWER_KW_RATE = 3.2425
    const val OIE_RATE = 0.0007
    const val VAT = 0.17
    const val BLOCK_I = 500
    const val BLOCK_II = 1500

    val vt = doubleArrayOf(0.0813, 0.1277, 0.2425)
    val mt = doubleArrayOf(0.0406, 0.0638, 0.1212)
    val transmission = doubleArrayOf(0.0120, 0.0060)
    val distribution = doubleArrayOf(0.0673, 0.0337)
}

data class BillResult(
    val blocks: List<BlockBreakdown>,
    val totalKwh: Double,
    val mjernoMjesto: Double,
    val obracunskaSnaga: Double,
    val serviceFee: Double,
    val totalEnergy: Double,
    val transmissionBaseCost: Double,
    val totalTransmission: Double,
    val distributionBaseCost: Double,
    val totalDistribution: Double,
    val totalOie: Double,
    val subtotal: Double,
    val vatAmount: Double,
    val total: Double,
    val consumptionKwh: Double,
    val isPartial: Boolean = false
)

object TariffCalculator {

    fun splitBlocks(totalKwh: Double): Triple<Double, Double, Double> {
        val blockI = minOf(totalKwh, 500.0)
        val blockII = minOf(maxOf(totalKwh - 500.0, 0.0), 1000.0)
        val blockIII = maxOf(totalKwh - 1500.0, 0.0)
        return Triple(blockI, blockII, blockIII)
    }

    fun calculateBill(
        vtKwh: Double,
        mtKwh: Double,
        approvedKw: Double = 3.3,
        daysInPeriod: Int? = null
    ): BillResult {
        if (!vtKwh.isFinite() || !mtKwh.isFinite() || vtKwh < 0 || mtKwh < 0) {
            return emptyResult()
        }

        val consumptionKwh = vtKwh + mtKwh
        if (consumptionKwh == 0.0) {
            return emptyResult()
        }

        val vtRatio = vtKwh / consumptionKwh
        val mtRatio = mtKwh / consumptionKwh

        val (blockIKwh, blockIIKwh, blockIIIKwh) = splitBlocks(consumptionKwh)

        val blocks = mutableListOf<BlockBreakdown>()

        if (blockIKwh > 0) {
            blocks.add(calculateBlock("Blok I (0-500 kWh)", blockIKwh, vtRatio, mtRatio, 0))
        }
        if (blockIIKwh > 0) {
            blocks.add(calculateBlock("Blok II (501-1500 kWh)", blockIIKwh, vtRatio, mtRatio, 1))
        }
        if (blockIIIKwh > 0) {
            blocks.add(calculateBlock("Blok III (1501+ kWh)", blockIIIKwh, vtRatio, mtRatio, 2))
        }

        val totalEnergy = blocks.sumOf { it.activeEnergyCost }
        val transmissionBaseCost = blocks.sumOf { it.transmissionCost }
        val distributionBaseCost = blocks.sumOf { it.distributionCost }
        val totalOie = blocks.sumOf { it.oieCost }

        val isPartial = daysInPeriod != null && daysInPeriod < 29

        val includedTransmissionPowerFee: Double
        val includedDistributionPowerFee: Double
        val includedFixedCharges: Double

        if (isPartial) {
            includedTransmissionPowerFee = 0.0
            includedDistributionPowerFee = 0.0
            includedFixedCharges = 0.0
        } else {
            includedTransmissionPowerFee = roundMoney(approvedKw * TariffRates.POWER_FLAT_RATE)
            includedDistributionPowerFee = roundMoney(approvedKw * TariffRates.POWER_KW_RATE)
            includedFixedCharges = TariffRates.SERVICE_FEE
        }

        val totalTransmission = roundMoney(transmissionBaseCost + includedTransmissionPowerFee)
        val totalDistribution = roundMoney(distributionBaseCost + includedDistributionPowerFee)

        val subtotal = roundMoney(
            includedFixedCharges + totalEnergy + totalTransmission + totalDistribution + totalOie
        )
        val vatAmount = roundMoney(subtotal * TariffRates.VAT)
        val total = roundMoney(subtotal + vatAmount)

        return BillResult(
            blocks = blocks,
            totalKwh = consumptionKwh,
            mjernoMjesto = if (isPartial) 0.0 else TariffRates.SERVICE_FEE,
            obracunskaSnaga = if (isPartial) 0.0 else roundMoney(
                approvedKw * TariffRates.POWER_FLAT_RATE + approvedKw * TariffRates.POWER_KW_RATE
            ),
            serviceFee = includedFixedCharges,
            totalEnergy = totalEnergy,
            transmissionBaseCost = transmissionBaseCost,
            totalTransmission = totalTransmission,
            distributionBaseCost = distributionBaseCost,
            totalDistribution = totalDistribution,
            totalOie = totalOie,
            subtotal = subtotal,
            vatAmount = vatAmount,
            total = total,
            consumptionKwh = consumptionKwh,
            isPartial = isPartial
        )
    }

    private fun calculateBlock(
        label: String,
        kwh: Double,
        vtRatio: Double,
        mtRatio: Double,
        blockIndex: Int
    ): BlockBreakdown {
        val activeEnergyCost = roundMoney(
            kwh * vtRatio * TariffRates.vt[blockIndex] +
                    kwh * mtRatio * TariffRates.mt[blockIndex]
        )
        val transmissionCost = roundMoney(
            kwh * vtRatio * TariffRates.transmission[0]
        ) + roundMoney(
            kwh * mtRatio * TariffRates.transmission[1]
        )
        val distributionCost = roundMoney(
            kwh * vtRatio * TariffRates.distribution[0]
        ) + roundMoney(
            kwh * mtRatio * TariffRates.distribution[1]
        )
        val oieCost = roundMoney(kwh * vtRatio * TariffRates.OIE_RATE) +
                roundMoney(kwh * mtRatio * TariffRates.OIE_RATE)
        val totalCost = activeEnergyCost + transmissionCost + distributionCost + oieCost
        val rate = if (kwh > 0) activeEnergyCost / kwh else 0.0

        return BlockBreakdown(
            label = label,
            kwh = kwh,
            rate = rate,
            activeEnergyCost = activeEnergyCost,
            transmissionCost = transmissionCost,
            distributionCost = distributionCost,
            oieCost = oieCost,
            totalCost = totalCost
        )
    }

    private fun emptyResult() = BillResult(
        blocks = emptyList(),
        totalKwh = 0.0,
        mjernoMjesto = 0.0,
        obracunskaSnaga = 0.0,
        serviceFee = 0.0,
        totalEnergy = 0.0,
        transmissionBaseCost = 0.0,
        totalTransmission = 0.0,
        distributionBaseCost = 0.0,
        totalDistribution = 0.0,
        totalOie = 0.0,
        subtotal = 0.0,
        vatAmount = 0.0,
        total = 0.0,
        consumptionKwh = 0.0
    )

    private fun roundMoney(value: Double): Double {
        return Math.round((value + Double.MIN_VALUE) * 100.0) / 100.0
    }
}
