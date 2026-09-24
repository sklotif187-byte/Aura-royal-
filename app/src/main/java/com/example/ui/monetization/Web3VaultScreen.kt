package com.example.ui.monetization

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppDataStore
import com.example.model.BlockchainTx
import com.example.model.SubscriptionTier
import com.example.ui.theme.*

@Composable
fun Web3VaultScreen() {
    var selectedMintTier by remember { mutableStateOf<SubscriptionTier?>(null) }
    var showWithdrawModal by remember { mutableStateOf(false) }

    val wallet = AppDataStore.wallet
    val tiers = AppDataStore.subscriptionTiers
    val txs = AppDataStore.transactions

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Vault Header: Web3 Smart Wallet
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, GoldPrimary, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess)
                            )
                            Text(
                                text = "Web3 Multi-Chain Vault",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                        }

                        Text(
                            text = wallet.ethAddress,
                            fontSize = 11.sp,
                            color = TextMuted,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ObsidianElevated)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Total Creator Net Worth", fontSize = 11.sp, color = TextSecondary)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$34,280.00",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+19.4% this month",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSuccess,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Multi-token balances
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TokenPill(symbol = "AURA", amount = "${wallet.auraBalance.toInt()}", valueUsd = "$14,250", color = GoldPrimary, modifier = Modifier.weight(1f))
                        TokenPill(symbol = "ETH", amount = "${wallet.ethBalance}", valueUsd = "$14,800", color = VioletCrypto, modifier = Modifier.weight(1f))
                        TokenPill(symbol = "USDC", amount = "${wallet.usdcBalance.toInt()}", valueUsd = "$8,450", color = BlueVerified, modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showWithdrawModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Text("Instant Payout / Cashout", color = ObsidianBg, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = ObsidianElevated),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
                        ) {
                            Text("Swap to Fiat", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Section: Exclusive Creator Passes & Subscriptions
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Exclusive Content Subscriptions",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                        Text(
                            text = "Token-Gated Creator Passes & MRR",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Text(
                        text = "MRR: $22,834/mo",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSuccess,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ObsidianElevated)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Tier Cards
        items(tiers) { tier ->
            TierCard(
                tier = tier,
                onMint = { selectedMintTier = tier }
            )
        }

        // Section: On-Chain Transaction Ledger
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Blockchain Transaction Ledger",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                        Text(
                            text = "Immutable On-Chain Verification",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Text("Explorer ↗", fontSize = 11.sp, color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Ledger Items
        items(txs) { tx ->
            TxRow(tx = tx)
        }
    }

    // Mint Pass Dialog
    selectedMintTier?.let { tier ->
        MintPassDialog(
            tier = tier,
            onDismiss = { selectedMintTier = null }
        )
    }

    // Withdraw Modal
    if (showWithdrawModal) {
        WithdrawDialog(
            onDismiss = { showWithdrawModal = false }
        )
    }
}

@Composable
fun TokenPill(
    symbol: String,
    amount: String,
    valueUsd: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianElevated)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Text(symbol, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(amount, fontSize = 13.sp, fontWeight = FontWeight.Black, color = GoldLight)
            Text(valueUsd, fontSize = 10.sp, color = TextMuted)
        }
    }
}

@Composable
fun TierCard(
    tier: SubscriptionTier,
    onMint: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (tier.isSubscribed) GoldPrimary else ObsidianBorder,
                RoundedCornerShape(16.dp)
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(tier.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        if (tier.isSubscribed) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "ACTIVE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = ObsidianBg,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GoldPrimary)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text("${tier.activeSubscribers} worldwide subscribers", fontSize = 11.sp, color = TextSecondary)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("$${tier.priceUsd.toInt()}/mo", fontSize = 18.sp, fontWeight = FontWeight.Black, color = GoldLight)
                    Text("or ${tier.priceEth} ETH", fontSize = 10.sp, color = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(tier.description, fontSize = 12.sp, color = TextSecondary, lineHeight = 16.sp)

            Spacer(modifier = Modifier.height(10.dp))

            // Perks list
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                tier.perks.forEach { perk ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(13.dp))
                        Text(perk, fontSize = 11.sp, color = TextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onMint,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (tier.isSubscribed) ObsidianElevated else GoldPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (tier.isSubscribed) "Manage Pass NFT" else "Mint Pass for $${tier.priceUsd.toInt()} USDC",
                    color = if (tier.isSubscribed) GoldLight else ObsidianBg,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun TxRow(tx: BlockchainTx) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianCard)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ObsidianElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (tx.type) {
                        "CREATOR_TIP" -> Icons.Default.Bolt
                        "PASS_MINT" -> Icons.Default.ConfirmationNumber
                        else -> Icons.Default.AccountBalanceWallet
                    },
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = tx.type.replace("_", " "),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Tx: ${tx.hash} · Block #${tx.blockNumber}",
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${tx.amount} ${tx.token}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = EmeraldSuccess
            )
            Text(
                text = tx.timestamp,
                fontSize = 10.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun MintPassDialog(
    tier: SubscriptionTier,
    onDismiss: () -> Unit
) {
    var isConfirmed by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GoldPrimary, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isConfirmed) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = ObsidianBg, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Pass Minted Successfully! 👑", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "You now have unrestricted VIP backstage access to all token-gated 4K videos, raw timelines, and masterclass chat.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enter VIP Lounge", color = ObsidianBg, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Mint Creator Pass", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(tier.name, fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    Text("$${tier.priceUsd.toInt()} USDC / month", fontSize = 14.sp, color = GoldPrimary, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Smart Contract: 0x9812...F42A\nGas Fee: ~0.0004 ETH (~$1.10)",
                        fontSize = 11.sp,
                        color = TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            isConfirmed = true
                            AppDataStore.transactions.add(
                                0,
                                BlockchainTx(
                                    hash = "0x" + (1000..9999).random().toString(16) + "...pass",
                                    blockNumber = 20491890,
                                    type = "PASS_MINT",
                                    amount = "+${tier.priceUsd.toInt()}",
                                    token = "USDC",
                                    from = AppDataStore.wallet.ethAddress,
                                    to = "AURA Vault Smart Contract",
                                    timestamp = "Just now",
                                    status = "CONFIRMED"
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Confirm Web3 Transaction", color = ObsidianBg, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun WithdrawDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            modifier = Modifier.fillMaxWidth().border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Instant Creator Payout", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Transfer earnings directly to your verified bank account or external cold wallet (Ledger / Trezor) with zero platform fee.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Withdraw $8,450 USDC to Bank", color = ObsidianBg, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
