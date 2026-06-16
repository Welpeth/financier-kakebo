/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 4.1.1 on 2026-06-15 20:25:52.

export interface CreateAccountRequest {
    name: string;
    isActive: boolean;
    balance: number;
    type: AccountType;
}

export interface UpdateAccountRequest {
    id: string;
    name: string;
    isActive: boolean;
    balance: number;
    type: AccountType;
}

export interface Account extends BaseEntity {
    name: string;
    isActive: boolean;
    balance: number;
    type: AccountType;
    holder: Holder;
}

export interface QAccount extends EntityPathBase<Account> {
    _super: QBaseEntity;
    accountCards: ListPath<AccountCard, QAccountCard>;
    balance: NumberPath<number>;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    holder: QHolder;
    id: ComparablePath<string>;
    isActive: BooleanPath;
    name: StringPath;
    transactions: ListPath<Transaction, QTransaction>;
    updatedAt: DateTimePath<Date>;
}

export interface AvailableLimitResponse {
    creditLimit: number;
    usedAmount: number;
    availableLimit: number;
}

export interface CreateAccountCardRequest {
    name: string;
    isActive: boolean;
    type: CardType;
    creditLimit: number;
    expirationMonth: number;
    expirationYear: number;
    account: Account;
}

export interface UpdateAccountCardRequest {
    id: string;
    name: string;
    isActive: boolean;
    type: CardType;
    creditLimit: number;
    expirationMonth: number;
    expirationYear: number;
}

export interface AccountCard extends BaseEntity {
    name: string;
    isActive: boolean;
    type: CardType;
    creditLimit: number;
    expirationMonth: number;
    expirationYear: number;
    account: Account;
}

export interface QAccountCard extends EntityPathBase<AccountCard> {
    _super: QBaseEntity;
    account: QAccount;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    creditLimit: NumberPath<number>;
    expirationMonth: NumberPath<number>;
    expirationYear: NumberPath<number>;
    id: ComparablePath<string>;
    isActive: BooleanPath;
    name: StringPath;
    transactions: ListPath<Transaction, QTransaction>;
    updatedAt: DateTimePath<Date>;
}

export interface CreateAddressRequest {
}

export interface Address extends BaseEntity {
    holders: HolderAddress[];
}

export interface QAddress extends EntityPathBase<Address> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    holders: ListPath<HolderAddress, QHolderAddress>;
    id: ComparablePath<string>;
    updatedAt: DateTimePath<Date>;
}

export interface CreateCategoryRequest {
    name: string;
}

export interface UpdateCategoryRequest {
    id: string;
    name: string;
}

export interface Category extends BaseEntity {
    name: string;
}

export interface QCategory extends EntityPathBase<Category> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    name: StringPath;
    updatedAt: DateTimePath<Date>;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    token: string;
}

export interface RegisterRequest {
    name: string;
    email: string;
    password: string;
}

export interface Holder extends BaseEntity {
    name: string;
    email: string;
}

export interface QHolder extends EntityPathBase<Holder> {
    _super: QBaseEntity;
    accounts: ListPath<Account, QAccount>;
    addresses: ListPath<HolderAddress, QHolderAddress>;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    email: StringPath;
    id: ComparablePath<string>;
    name: StringPath;
    password: StringPath;
    updatedAt: DateTimePath<Date>;
}

export interface CreateHolderAddressRequest {
    holder: Holder;
    address: Address;
}

export interface HolderAddress {
    id: HolderAddressId;
    holder: Holder;
    address: Address;
}

export interface HolderAddressId extends Serializable {
    idHolder: string;
    idAddress: string;
}

export interface QHolderAddress extends EntityPathBase<HolderAddress> {
    address: QAddress;
    holder: QHolder;
    id: QHolderAddressId;
}

export interface QHolderAddressId extends BeanPath<HolderAddressId> {
    idAddress: ComparablePath<string>;
    idHolder: ComparablePath<string>;
}

export interface CreateInstallmentListRequest {
    installmentPurchase: InstallmentPurchase;
    installmentNumber: number;
    amount: number;
    fee: number;
    dueDate: Date;
    installmentType: InstallmentType;
}

export interface CreateInstallmentRequest {
    installmentPurchase: InstallmentPurchase;
    installmentNumber: number;
    amount: number;
    dueDate: Date;
}

export interface UpdateInstallmentRequest {
    id: string;
    amount: number;
    dueDate: Date;
    paid: boolean;
    paidAt: Date;
}

export interface Installment extends BaseEntity {
    installmentNumber: number;
    amount: number;
    dueDate: Date;
    paid: boolean;
    paidAt: Date;
    installmentPurchase: InstallmentPurchase;
}

export interface QInstallment extends EntityPathBase<Installment> {
    _super: QBaseEntity;
    amount: NumberPath<number>;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    dueDate: DatePath<Date>;
    id: ComparablePath<string>;
    installmentNumber: NumberPath<number>;
    installmentPurchase: QInstallmentPurchase;
    paid: BooleanPath;
    paidAt: DatePath<Date>;
    updatedAt: DateTimePath<Date>;
}

export interface CreateInstallmentPurchaseRequest {
    transaction: Transaction;
    totalAmount: number;
    installmentCount: number;
    interestRate: number;
}

export interface UpdateInstallmentPurchaseRequest {
    id: string;
    totalAmount: number;
    installmentCount: number;
    interestRate: number;
}

export interface InstallmentPurchase extends BaseEntity {
    totalAmount: number;
    interestRate: number;
    totalAmountWithInterest: number;
    transaction: Transaction;
}

export interface QInstallmentPurchase extends EntityPathBase<InstallmentPurchase> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    installments: ListPath<Installment, QInstallment>;
    interestRate: NumberPath<number>;
    totalAmount: NumberPath<number>;
    totalAmountWithInterest: NumberPath<number>;
    transaction: QTransaction;
    updatedAt: DateTimePath<Date>;
}

export interface CreateJournalRequest {
    name: string;
}

export interface GetJournalRequest {
    id: string;
}

export interface UpdateJournalRequest {
    id: string;
    name: string;
}

export interface Journal extends BaseEntity {
    name: string;
    totalValue: number;
}

export interface QJournal extends EntityPathBase<Journal> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    ledgerEntries: ListPath<LedgerEntry, QLedgerEntry>;
    name: StringPath;
    totalValue: NumberPath<number>;
    updatedAt: DateTimePath<Date>;
}

export interface CreateLedgerEntryRequest {
    name: string;
    finalDate: Date;
    journal: Journal;
    transaction: Transaction;
}

export interface UpdateLedgerEntryRequest {
    id: string;
    name: string;
    finalDate: Date;
}

export interface LedgerEntry extends BaseEntity {
    name: string;
    finalDate: Date;
    journal: Journal;
    transaction: Transaction;
}

export interface QLedgerEntry extends EntityPathBase<LedgerEntry> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    finalDate: DateTimePath<Date>;
    id: ComparablePath<string>;
    journal: QJournal;
    name: StringPath;
    transaction: QTransaction;
    updatedAt: DateTimePath<Date>;
}

export interface CreateSubscriptionRequest {
    transaction: Transaction;
    frequency: SubscriptionFrequency;
    nextChargeDate: Date;
}

export interface UpdateSubscriptionRequest {
    id: string;
    frequency: SubscriptionFrequency;
    nextChargeDate: Date;
    active: boolean;
}

export interface QSubscription extends EntityPathBase<Subscription> {
    _super: QBaseEntity;
    active: BooleanPath;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    frequency: EnumPath<SubscriptionFrequency>;
    id: ComparablePath<string>;
    nextChargeDate: DatePath<Date>;
    transaction: QTransaction;
    updatedAt: DateTimePath<Date>;
}

export interface Subscription extends BaseEntity {
    frequency: SubscriptionFrequency;
    nextChargeDate: Date;
    active: boolean;
    transaction: Transaction;
}

export interface CreateTransactionRequest {
    account: Account;
    accountCard: AccountCard;
    category: Category;
    type: TransactionType;
    amount: number;
    fee: number;
    installment: number;
    description: string;
    dueDate: Date;
    frequency: SubscriptionFrequency;
    installmentType: InstallmentType;
}

export interface UpdateTransactionRequest {
    id: string;
    account: Account;
    accountCard: AccountCard;
    category: Category;
    type: TransactionType;
    amount: number;
    fee: number;
    installment: number;
    description: string;
    installmentType: InstallmentType;
}

export interface QTransaction extends EntityPathBase<Transaction> {
    _super: QBaseEntity;
    account: QAccount;
    accountCard: QAccountCard;
    amount: NumberPath<number>;
    category: QCategory;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    description: StringPath;
    fee: NumberPath<number>;
    id: ComparablePath<string>;
    installment: NumberPath<number>;
    installmentType: EnumPath<InstallmentType>;
    ledgerEntries: ListPath<LedgerEntry, QLedgerEntry>;
    subscription: BooleanPath;
    updatedAt: DateTimePath<Date>;
}

export interface Transaction extends BaseEntity {
    amount: number;
    fee: number;
    installment: number;
    type: TransactionType;
    description: string;
    subscription: boolean;
    installmentType: InstallmentType;
    account: Account;
    accountCard: AccountCard;
    category: Category;
}

export interface BaseEntity {
    id: string;
    createdAt: Date;
    createdBy: string;
    updatedAt: Date;
}

export interface QBaseEntity extends EntityPathBase<BaseEntity> {
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    updatedAt: DateTimePath<Date>;
}

export interface ListPath<E, Q> extends CollectionPathBase<E[], E, Q>, ListExpression<E, Q> {
}

export interface NumberPath<T> extends NumberExpression<T>, Path<T> {
}

export interface DateTimePath<T> extends DateTimeExpression<T>, Path<T> {
}

export interface StringPath extends StringExpression, Path<string> {
}

export interface ComparablePath<T> extends ComparableExpression<T>, Path<T> {
}

export interface BooleanPath extends BooleanExpression, Path<boolean> {
}

export interface Class<T> extends Serializable, GenericDeclaration, Type, AnnotatedElement, OfField<Class<any>>, Constable {
}

export interface Path<T> extends Expression<T> {
    root: Path<any>;
    annotatedElement: AnnotatedElement;
    metadata: PathMetadata;
}

export interface AnnotatedElement {
    annotations: Annotation[];
    declaredAnnotations: Annotation[];
}

export interface PathMetadata extends Serializable {
    element: any;
    parent: Path<any>;
    rootPath: Path<any>;
    pathType: PathType;
    name: string;
    root: boolean;
}

export interface Serializable {
}

export interface DatePath<T> extends DateExpression<T>, Path<T> {
}

export interface EnumPath<T> extends EnumExpression<T>, Path<T> {
}

export interface StringExpression extends LiteralExpression<string> {
}

export interface BooleanExpression extends LiteralExpression<boolean>, Predicate {
}

export interface GenericDeclaration extends AnnotatedElement {
    typeParameters: TypeVariable<any>[];
}

export interface Type {
    typeName: string;
}

export interface Constable {
}

export interface Annotation {
}

export interface EntityPathBase<T> extends BeanPath<T>, EntityPath<T> {
}

export interface BeanPath<T> extends SimpleExpression<T>, Path<T> {
}

export interface CollectionPathBase<C, E, Q> extends CollectionExpressionBase<C, E>, Path<C> {
}

export interface ListExpression<E, Q> extends CollectionExpression<E[], E> {
}

export interface NumberExpression<T> extends ComparableExpressionBase<T> {
}

export interface DateTimeExpression<T> extends TemporalExpression<T> {
}

export interface ComparableExpression<T> extends ComparableExpressionBase<T> {
}

export interface Predicate extends Expression<boolean> {
}

export interface TypeVariable<D> extends Type, AnnotatedElement {
    name: string;
    bounds: Type[];
    genericDeclaration: D;
    annotatedBounds: AnnotatedType[];
}

export interface OfField<F> extends TypeDescriptor {
    array: boolean;
    primitive: boolean;
}

export interface Expression<T> extends Serializable {
    type: Class<T>;
}

export interface DateExpression<T> extends TemporalExpression<T> {
}

export interface EnumExpression<T> extends LiteralExpression<T> {
}

export interface LiteralExpression<T> extends ComparableExpression<T> {
}

export interface AnnotatedType extends AnnotatedElement {
    type: Type;
    annotatedOwnerType: AnnotatedType;
}

export interface TypeDescriptor {
}

export interface EntityPath<T> extends Path<T> {
}

export interface SimpleExpression<T> extends DslExpression<T> {
}

export interface CollectionExpressionBase<T, E> extends DslExpression<T>, CollectionExpression<T, E> {
    elementType: Class<E>;
}

export interface CollectionExpression<T, E> extends ParameterizedExpression<T> {
}

export interface ComparableExpressionBase<T> extends SimpleExpression<T> {
}

export interface TemporalExpression<T> extends LiteralExpression<T> {
}

export interface DslExpression<T> extends Expression<T> {
}

export interface ParameterizedExpression<T> extends Expression<T> {
}

export type AccountType = "CHECKING" | "SAVINGS";

export type CardType = "CREDIT" | "DEBIT";

export type InstallmentType = "PRICE" | "SAC";

export type SubscriptionFrequency = "DAILY" | "WEEKLY" | "MONTHLY" | "QUARTERLY" | "YEARLY";

export type TransactionType = "CASH" | "DEBIT" | "CREDIT" | "PIX";

export type PathType = "ARRAYVALUE" | "ARRAYVALUE_CONSTANT" | "COLLECTION_ANY" | "DELEGATE" | "LISTVALUE" | "LISTVALUE_CONSTANT" | "MAPVALUE" | "MAPVALUE_CONSTANT" | "PROPERTY" | "VARIABLE" | "TREATED_PATH";
