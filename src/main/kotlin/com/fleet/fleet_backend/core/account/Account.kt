package com.fleet.fleet_backend.core.account

import javax.persistence.*

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: String,

    @Column(name = "username", unique = true, nullable = false) val username: String,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinTable(name = "employee_contacts_info",
        joinColumns = [JoinColumn(name = "account_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "contact_info_id", referencedColumnName = "id")])
    val contactInfo: ContactInfo
)

@Entity
@Table(name = "contacts_info")
data class ContactInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: String,
    @OneToOne(mappedBy = "contactInfo") val account: Account,
    @Column(unique = true, nullable = false) val email: String
)
